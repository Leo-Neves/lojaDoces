'use strict'

const express = require('express');
const router = express.Router();
const controller = require('../controllers/pedido-controller');
const uploadService = require('../services/upload-service');

router.get('/', controller.get);
router.get('/:id', controller.getById);
router.post('/', controller.post);
router.post('/:id', uploadService.upload('pedidos','file', controller.pluginFileFilter), controller.postArquivo);
router.put('/:id', controller.put);
router.delete('/:id', controller.delete);

module.exports = router;