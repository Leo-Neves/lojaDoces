'use strict'

const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const config = require('./config');

const app = express();
const router = express.Router();

//Conecta ao banco
mongoose.connect(config.connectionString)

//Carrega os Models
const Produto = require('./models/produto');
const Pedido = require('./models/pedido');
const Ponto = require('./models/ponto');
const Usuario = require('./models/usuario');

//Carrega as rotas
const index = require('./routes/index');
const produtos = require('./routes/produtos');
const pedidos = require('./routes/pontos');
const pontos = require('./routes/pontos');
const usuarios = require('./routes/usuarios')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/', index);
app.use('/produtos', produtos);
app.use('/pedidos', pedidos);
app.use('/pontos', pontos);
app.use('/', usuarios);

module.exports = app;