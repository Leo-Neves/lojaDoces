'use strict'

const mongoose = require('mongoose');
const uuid = require('uuid/v4');
const Schema = mongoose.Schema;
require('mongoose-uuid2')(mongoose);

const schema = new Schema({
    _id: {
        type: mongoose.Types.UUID,
        default: uuid()
    },
     usuario_id: {
        type: String,
        required: [true, 'O usuário é obrigatório'],
        trim: true
     },
     valor:{
        type: Number,
        required: [true, 'O valor é obrigatório'],
     },
     pedido_id: {
        type: String,
        required: [true, 'O pedido é obirgatório'],
        trim: true
     },
     validade: {
        type: String,
        required: [true, 'A validade é obrigatória']
     }
}, { id: false });
schema.set('toObject', {getters: true});
schema.set('toJSON', {getters: true});
module.exports = mongoose.model('Ponto', schema);