var webAppUrl = "https://script.google.com/macros/s/AKfycbxasdx213WgwnL4VI9kr5DM2mcyaHSY0nDhZWAzFnLvrdJnULpSAmeBZ1LeNL6bMUGNq6V_8Z-w/exec";

var ssid = "1ORAkvB1h8kuryfFdPx5avptfen-Bzv06dI6cSB_HMqA";
var sheet = SpreadsheetApp.openById(ssid);

function doGet(request) {
  return 0;
}

// where the telegram work
function doPost(e) {
  // Logger.log(e);
  var action = e.parameter.action;
  var email = e.parameter.email;

  // check Email 
  if(action == 'checkEmail'){
     return handleUploadAndCheckEmail(email);
  }
}