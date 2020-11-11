var MATERIAL_CODE = {
    chanpindalei: 0,
    guigexinghao: 1,
    barcode: 2,
    weight: 3,
    produceDate: 4,
    haoshupiancha: 5,
    jietoufangshi: 6
}
var ROLL_CODE = {
    barcode: 0,
    salesOrderCode: 1,
    salesProductId: 2

}
function parseMaterialCode(qrcode, code) {
    var qrarray = qrcode.split(";");
    if (code == MATERIAL_CODE.chanpindalei) {
        return $.base64.decode(qrarray[code], true)

    }
    if (code == MATERIAL_CODE.guigexinghao) {
        return $.base64.decode(qrarray[code], true)
    }
    if (code == MATERIAL_CODE.produceDate) {
        return new Date(qrarray[code]);
    }
    return qrarray[code];
}
function parseRollCode(qrcode, code) {
    var qrarray = qrcode.split(";");
    return qrarray[code];
}

function formatMateriaCode(code){
    var reg = /^[\'\"]+|[\'\"]+$/g;
    code = code.replace(reg,"");
    if(code.indexOf("{") == 0 && code.indexOf("salverNumver") != -1){
        code = JSON.parse(code).salverNumver;
    }
    return code;
}