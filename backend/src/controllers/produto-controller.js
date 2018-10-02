'use strict'

const ValidationContract = require('../validators/fluent-validator');
const mongoose = require('mongoose');
const Produto = mongoose.model('Produto');
const repository = require('../repositories/produto-repository');

exports.get = (req, res, next) => {
    repository.findAll()
    .then(data => {
        res.status(200).send(data);
    })
    .catch(e => {
        sendError(e, res);
    });
};

exports.getById = (req, res, next) => {
    repository.findById(req.params.id)
        .then(data => {
            res.status(200).send(data);
        })
        .catch(e => {
            sendError(e, res);
        });
};

exports.getByCategorias = async(req, res, next) => {
    try{
        var data = await repository.findByCategorias(req.params.categoria);
        res.status(200).send(data);
    }catch(e){
        sendError(e, res);
    }
};

exports.post = async(req, res, next) => {
    let contract = new ValidationContract();
    contract.isRequired(req.body.nome, 'O nome é obrigatório');
    contract.hasMinLen(req.body.nome, 4, 'O nome deve conter pelo menos 4 caracteres');

    if (!contract.isValid()){
        res.status(400).send(contract.errors()).end();
        return;
    }
    repository
        .save(req.body)
        .then(data => {
            res.status(201).send(data);
        })
        .catch(e =>{
            sendError(e, res)
        });
};

exports.postArquivo = async(req, res, next) => {
    console.log(req.file);
    repository
        .updatePluginFile(req.params.id, req.file.path)
        .then(x => {
            res.status(200).send();
        })
        .catch(e => {
            sendError(e, res);
        });
}

exports.pluginFileFilter = async(req, file, cb) => {
    Produto
        .findOne({_id: req.params.id})
        .count({}, function( err, count){
                if (count>=1){
                    cb(null,true);
                }else{
                    cb(new Error('ID não encontrado'),false);
                }
            });
}

exports.put = async(req, res, next) => {
    try{
        let data = await repository.update(req.params.id, req.body.nome, req.body.valor, req.body.categorias);
        res.status(201).send(data);
    }catch(e){
        sendError(e, res);
    }
};

exports.delete = async(req, res, next) => {
    repository
        .delete(req.params.id)
        .then(data => {
            res.status(200).send({message: "Produto removido com sucesso"});
        })
        .catch(e => {
            sendError(e, res);
        });;
};

var sendError = (e, res) =>{
    const json = JSON.parse(JSON.stringify(e));
    const codigo = json.hasOwnProperty('codigo')? json.codigo : 500;
    const mensagem = json.hasOwnProperty('mensagem')?json.mensagem : "Falha ao processar a requisição!";
    res.status(codigo).send({
        mensagem: mensagem
    });
    console.log(e);
}