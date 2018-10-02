'use strict'
const mongoose = require('mongoose');
const Usuario = mongoose.model('Usuario');
const RESTException = require('../validators/rest-exception');

exports.findTokenByEmailSenha = async(email, senha) => {
    let usuario =  Usuario.findOne({
        email: email,
        senha: senha
    });
    let hasUsuario = false;
    await usuario.count({}, function( err, count){
        if (count === 1) hasUsuario = true;
    });
    if (!hasUsuario){
        throw new RESTException().loginRecusado();
    }
    return Usuario.findOne({
        email: email,
        senha: senha
    },'_id nome email');
}

exports.save = async(nome, email, senha) =>{
    let usuario = Usuario.findOne({
        email: email
    });
    let hasUsuario = false;
    await usuario.count({}, function( err, count){
        if (count == 1) hasUsuario = true;
    })
    if (!hasUsuario){
        let usuario = new Usuario({
            nome: nome,
            email: email,
            senha: senha
        });
        usuario.save();
        return usuario;
    }else{
        throw new RESTException().emailJaCadastrado();
    }
}