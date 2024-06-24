function PrintSlip() {
    AndroidFunction.printSlipNative(qrcode);
    AndroidFunction.showToast('กำลังพิมพ์ใบเสร็จ');
}