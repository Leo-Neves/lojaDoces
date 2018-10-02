'use strict'
const multer = require('multer');

exports.upload = (folder, uploadKey, fileFilter) =>{
    let storage = multer.diskStorage({
        destination: (req, file, cb) =>{
            cb(null, './uploads/'+folder);
        },
        filename: (req, file, cb) => {
            let regexExtension = /\.[0-9a-z]+$/i;
            let name = req.params.id + file.originalname.match(regexExtension);
            cb(null, name);
        }
    });
    let limits = {
        fileSize: 1024 * 1024 * 5
    };
    let upload = multer({
        storage: storage,
        limits: limits,
        fileFilter: fileFilter
    });
    return upload.single(uploadKey);
}