'use strict'
const mongoose = require('mongoose');
const Produto = mongoose.model('Produto');
const RESTException = require('../validators/rest-exception');
const fs = require('fs');

exports.findAll = () => {
    const result = Produto.find({
        }, '_id nome valor categorias');
    return result;
};

exports.findById = async(id) => {
    let hasProduto = await exports.doesProdutoExists(id);
    if (hasProduto)
        return Produto.findOne({
                _id: id
            }
        );
    throw new RESTException().idNaoEncontrado();
}

exports.findByCategorias = async(categorias) => {
    const result = await Produto.find({
        categorias: categorias
    },'_id nome valor categorias');
    return result;
}

exports.save = async(data) =>{
    const uuid = require('uuid/v4');
    console.log(uuid());
    return await new Produto(data).save();
}

exports.update = async(id, nome, valor, categorias) =>{
    if (await exports.doesProdutoExists(id)){
        let plugin = Produto.findOne({_id: id});
        plugin.update({
            $set: {
                nome: nome,
                valor: valor,
                categorias: categorias
            }
        }).exec();
    }else{
        throw new RESTException().idNaoEncontrado();
    }
    return Produto.findOne({_id: id});;
}

exports.updatePluginFile = async(id, path) => {
    // let hasPlugin = await exports.doesPluginExists(id);
    // if (hasPlugin){
    //     let plugin = await Plugin.findById(id);
    //     await plugin.update({
            
    //     }).exec();
    //     return plugin;
    // }
    return await Produto
        .findOne({_id: id})
        .update({$set:{
            imagem_path: path
        }});
    // throw new RESTException().idNaoEncontrado();
}

exports.delete = async(id) => {
     if (await exports.doesProdutoExists(id)){
        let produto = Produto.findOne({_id: id
        });
        produto.remove().exec(result =>{
            console.log(result);
        });
    }else{
        throw new RESTException().idNaoEncontrado();
    }
}

exports.doesProdutoExists = async(id) =>{
    let plugin = Produto.where('_id').equals(id);
    let hasProduto = false;
    await plugin.count({}, function( err, count){
        if (count >= 1) hasProduto = true;
        console.log('PRODUTOS SIZE: '+count);
    });
    return hasProduto;
}