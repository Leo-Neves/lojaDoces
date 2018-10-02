'use strict'
const mongoose = require('mongoose');
const Pedido = mongoose.model('Pedido');
const RESTException = require('../validators/rest-exception');
const fs = require('fs');

exports.findAll = () => {
    const result = Pedido.find({
        }, 'nome descricao desenvolvedor');
    return result;
};

exports.findById = async(id) => {
    let hasPlugin = await exports.doesPluginExists(id);
    if (hasPlugin)
        return Pedido.findOne({
                _id: id
            }
        );
    throw new RESTException().idNaoEncontrado();
}

exports.findBySensores = async(sensores) => {
    const result = await Pedido.find({
        sensores: sensores
    },'nome desenvolvedor descricao tamanho consumo_dados pagina_desenvolvedor imagem_url sensores');
    return result;
}

exports.save = async(data) =>{
    const uuid = require('uuid/v4');
    console.log(uuid());
    return await new Pedido(data).save();
}

exports.update = async(id, nome, descricao, desenvolvedor, tamanho, consumo_dados, sensores, pagina_desenvolvedor, imagem_url) =>{
    if (await exports.doesPluginExists(id)){
        let plugin = Pedido.findOne({_id: id});
        plugin.update({
            $set: {
                nome: nome,
                descricao: descricao,
                desenvolvedor: desenvolvedor,
                tamanho: tamanho,
                consumo_dados: consumo_dados,
                sensores: sensores,
                pagina_desenvolvedor: pagina_desenvolvedor,
                imagem_url: imagem_url
            }
        }).exec();
    }else{
        throw new RESTException().idNaoEncontrado();
    }
    return Pedido.findOne({_id: id});;
}

exports.updatePluginFile = async(id, path) => {
    // let hasPlugin = await exports.doesPluginExists(id);
    // if (hasPlugin){
    //     let plugin = await Plugin.findById(id);
    //     await plugin.update({
            
    //     }).exec();
    //     return plugin;
    // }
    return await Pedido
        .findOne({_id: id})
        .update({$set:{
            imagem_path: path
        }});
    // throw new RESTException().idNaoEncontrado();
}

exports.delete = async(id) => {
     if (await exports.doesPluginExists(id)){
        let plugin = Pedido.findOne({_id: id
        });
        plugin.remove().exec(result =>{
            console.log(result);
        });
    }else{
        throw new RESTException().idNaoEncontrado();
    }
}

exports.doesPluginExists = async(id) =>{
    let plugin = Pedido.where('_id').equals(id);
    let hasPlugin = false;
    await plugin.count({}, function( err, count){
        if (count >= 1) hasPlugin = true;
        console.log('PLUGINS SIZE: '+count);
    });
    return hasPlugin;
}