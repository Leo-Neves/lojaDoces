'use strict';

function RESTException() {
   
}

RESTException.prototype.idNaoEncontrado = () => {
    return {
        codigo: 400,
        mensagem: "ID não encontrado"
    };
}

RESTException.prototype.emailJaCadastrado = () => {
    return {
        codigo: 400,
        mensagem: "Email já cadastrado"
    }
}

RESTException.prototype.loginRecusado = () => {
    return {
        codigo: 400,
        mensagem: "Email ou senha incorretos"
    }
}

module.exports = RESTException;