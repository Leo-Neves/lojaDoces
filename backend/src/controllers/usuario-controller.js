'use strict'

const ValidationContract = require('../validators/fluent-validator');
const mongoose = require('mongoose');
const Plugin = mongoose.model('Usuario');
const repository = require('../repositories/usuario-repository');
const md5 = require('md5');
const emailService = require('../services/email-service');

exports.cadastrar = async(req, res, next) => {
    let contract = new ValidationContract();
    contract.isRequired(req.body.nome, 'O nome é obrigatório');
    contract.hasMinLen(req.body.nome, 4, 'O nome deve conter pelo menos 4 caracteres');
    contract.isRequired(req.body.email, 'Email é obrigatório');
    contract.isEmail(req.body.email, 'Email inválido');
    contract.isRequired(req.body.senha, 'Senha obrigatória');
    contract.hasMinLen(req.body.senha, 6, 'Senha deve conter no mínimo 6 caracteres');

    if (!contract.isValid()){
        res.status(400).send(contract.errors()).end();
        return;
    }
    try{
        let senha = md5(req.body.senha + global.SALT_KEY);
        var data = await repository.save(req.body.nome, req.body.email, senha);
        emailService.send(
            req.body.email,
            'MinhaUFPA cadastro',
            global.EMAIL_TMPL.replace(
                '{0}',
                req.body.nome
            )
        );
        res.status(201).send(data);
    }catch(e){
        const json = JSON.parse(JSON.stringify(e));
        const codigo = json.hasOwnProperty('codigo')? json.codigo : 500; 
        const mensagem = json.hasOwnProperty('mensagem')?json.mensagem : "Falha ao processar a requisição!"
        res.status(codigo).send({
            mensagem: mensagem
        });
    }
};

exports.acessar = async(req, res, next) => {
    try{
        var data = await repository.findTokenByEmailSenha(req.body.email, md5(req.body.senha + global.SALT_KEY));
        res.status(200).send(data);
    }catch(e){
        const json = JSON.parse(JSON.stringify(e));
        const codigo = json.hasOwnProperty('codigo')? json.codigo : 500; 
        const mensagem = json.hasOwnProperty('mensagem')?json.mensagem : "Falha ao processar a requisição!"
        res.status(codigo).send({
            mensagem: mensagem
        });
    }
};