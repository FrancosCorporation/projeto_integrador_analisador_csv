import http from 'node:http';
import { readFile, realpath, stat } from 'node:fs/promises';
import { extname, resolve, sep } from 'node:path';
import { fileURLToPath } from 'node:url';

const PORT = Number(process.env.PORT || 3000);
if (!Number.isInteger(PORT) || PORT < 0 || PORT > 65_535) throw new Error('PORT precisa ser um número entre 0 e 65535.');

const ROOT = await realpath(fileURLToPath(new URL('.', import.meta.url)));
const ARQUIVOS_PUBLICOS = new Set([
  'index.html',
  'style.css',
  'js/app.js',
  'js/csv.js',
  'js/estatistica.js',
  'data/Evasao.csv'
]);
const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.csv': 'text/csv; charset=utf-8'
};
const CABECALHOS_SEGURANCA = {
  'Content-Security-Policy': "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; connect-src 'self'; object-src 'none'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'",
  'Referrer-Policy': 'no-referrer',
  'X-Content-Type-Options': 'nosniff'
};

function responder(res, status, body, headers = {}) {
  res.writeHead(status, { ...CABECALHOS_SEGURANCA, ...headers });
  res.end(body);
}

function dentroDaPasta(raiz, caminho) {
  return caminho === raiz || caminho.startsWith(`${raiz}${sep}`);
}

const server = http.createServer(async (req, res) => {
  if (req.method !== 'GET' && req.method !== 'HEAD') {
    req.resume();
    responder(res, 405, 'Método não permitido', {
      Allow: 'GET, HEAD',
      'Content-Type': 'text/plain; charset=utf-8'
    });
    return;
  }

  let caminho;
  try {
    caminho = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
  } catch {
    responder(res, 400, 'Endereço inválido', { 'Content-Type': 'text/plain; charset=utf-8' });
    return;
  }

  if (caminho === '/') caminho = '/index.html';
  const relativo = caminho.replace(/^\/+/, '');
  if (!ARQUIVOS_PUBLICOS.has(relativo) || relativo.split('/').some((parte) => parte.startsWith('.') || parte.includes('\\'))) {
    responder(res, 404, 'Não encontrado', { 'Content-Type': 'text/plain; charset=utf-8' });
    return;
  }

  try {
    const arquivo = resolve(ROOT, relativo);
    if (!dentroDaPasta(ROOT, arquivo)) {
      responder(res, 404, 'Não encontrado', { 'Content-Type': 'text/plain; charset=utf-8' });
      return;
    }

    const caminhoReal = await realpath(arquivo);
    if (!dentroDaPasta(ROOT, caminhoReal) || !(await stat(caminhoReal)).isFile()) {
      responder(res, 404, 'Não encontrado', { 'Content-Type': 'text/plain; charset=utf-8' });
      return;
    }

    const dados = await readFile(caminhoReal);
    responder(res, 200, req.method === 'HEAD' ? undefined : dados, {
      'Cache-Control': 'no-store',
      'Content-Length': dados.length,
      'Content-Type': MIME[extname(caminhoReal)] || 'application/octet-stream'
    });
  } catch {
    responder(res, 404, 'Não encontrado', { 'Content-Type': 'text/plain; charset=utf-8' });
  }
});

server.listen(PORT, '127.0.0.1', () => {
  const endereco = server.address();
  const portaReal = endereco && typeof endereco === 'object' ? endereco.port : PORT;
  console.log(`Analisador CSV local em http://127.0.0.1:${portaReal}/`);
});
