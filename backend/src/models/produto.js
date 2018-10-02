'use strict'

const mongoose = require('mongoose');
const uuid = require('uuid/v4');
const Schema = mongoose.Schema;
require('mongoose-uuid2')(mongoose);

const schema = new Schema({
    _id: {
        type: mongoose.Types.UUID,
        default: require('uuid/v4')
    },
     nome: {
        type: String,
        required: [true, 'O nome é obrigatório'],
        trim: true
     },
     valor:{
        type: Number,
        required: [true, 'O valor é obrigatório'],
        trim: true
     },
     categorias: [{
        type: String,
        required: [true, 'Lista de categorias é obrigatória'],
        trim: true
     }]
}, { id: false });
schema.set('toObject', {getters: true});
schema.set('toJSON', {getters: true});
module.exports = mongoose.model('Produto', schema);