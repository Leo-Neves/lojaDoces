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
    data: {
        type: String,
        required: [true, 'A data é obrigatória'],
        trim: true
    },
     produtos: [{
        type: String,
        required: [true, 'Lista de produtos do pedido é obrigatória'],
        trim: true
     }]
}, { id: false });
schema.set('toObject', {getters: true});
schema.set('toJSON', {getters: true});
module.exports = mongoose.model('Pedido', schema);