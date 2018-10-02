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
     nome: {
        type: String,
        required: [true, 'O nome é obrigatório'],
        trim: true
     },
     email:{
        type: String,
        required: [true, 'Email é obrigatório'],
        trim: true
     },
     senha: {
        type: String,
        required: [true, 'A descrição é obrigatória'],
        trim: true
     }
}, { id: false });
schema.set('toObject', {getters: true});
schema.set('toJSON', {getters: true});
module.exports = mongoose.model('Usuario', schema);