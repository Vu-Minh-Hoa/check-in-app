function formatDate(dateTime) {
  var year = dateTime?.getFullYear();
  var month = ("0" + (dateTime?.getMonth() + 1)).slice(-2);
  var date = ("0" + dateTime?.getDate()).slice(-2);
  var hour = ("0" + dateTime?.getHours()).slice(-2);
  var min = ("0" + dateTime?.getMinutes()).slice(-2);
  var sec = ("0" + dateTime?.getSeconds()).slice(-2);
  var time = year + '-' + month + '-' + date + ' ' + hour + ':' + min + ':' + sec;
  return time;
}

//=========================================================================
//handle upload and check email
function handleUploadAndCheckEmail(email) {
  var sheet = SpreadsheetApp.openById(ssid).getSheetByName('Sheet1'); // Adjust this to the sheet where you are checking for the email
  var emailColumn = sheet.getRange("A:A"); // Get emails from column A
  var emailValues = emailColumn.getValues();
  
  for(var i = 0; i < emailValues.length; i++) {
    if(emailValues[i][0] == email) {
      // Email found, now updating role and timestamp
      sheet.getRange(i + 1, 2).setValue("true"); // Sets role to "true" in column B
      sheet.getRange(i + 1, 3).setValue(formatDate(new Date())); // Sets timestamp in column C
      var response = {
        "status": '1',
        "message": "Email updated"
      };
      return ContentService.createTextOutput(JSON.stringify(response)).setMimeType(ContentService.MimeType.JSON);
    }
  }
  
  // If the email wasn't found, return the 'not found' response
  var response = {
    "status": "0",
    "message": "Email not found"
  };
  return ContentService.createTextOutput(JSON.stringify(response)).setMimeType(ContentService.MimeType.JSON);
}
//==========================================================================

function saveToGSheet(message) {
  var fromId = message.from;
  var name = fromId;
  var content = message.content;
  var date = formatDate(new Date());

  if(content == null || content === '') {
    response = {"status": "error", "data": "Không tồn tại nội dung"};
    Logger.log(JSON.stringify(response));
    return HtmlService.createHtmlOutput(JSON.stringify(response, null, 4));
  }
  
  SpreadsheetApp.openById(ssid).getSheets()[1].appendRow([fromId, content ,date]);

  var response = {"status": "succes", "data": {"content": decodeURIComponent(content)}};
  return HtmlService.createHtmlOutput(JSON.stringify(response, null, 4));
}

function saveEmailScaned(message) {
  var fromId = message.from;
  var name = fromId;
  var date = formatDate(new Date());
  SpreadsheetApp.openById(ssid).getSheets()[2].appendRow([fromId ,date]);
  var response = {"status": "succes", "data": {"content": decodeURIComponent(content)}};
  return HtmlService.createHtmlOutput(JSON.stringify(response, null, 4));
}


// Function get Email with type (Sheet 1)
function getAllEmailGeneratedQR(){
  var sheet = SpreadsheetApp.openById(ssid).getSheets()[0];
  var startRow = 1;
  var startColumn = 1;
  var numColums = 2;
  var numRow = sheet.getLastRow();
  var dataRange = sheet.getRange(startRow,startColumn,numRow,numColums);
  var data = JSON.stringify(dataRange.getValues());
  Logger.log(data);
  return data;
}


// Function get email from 
function checkEmail(email){
   var getListEmail = getAllColumnA1WithScanned();
   var isCheck = false;
   getListEmail.sort();
   getListEmail.forEach(function(value) {
    if(email == value){
      isCheck = true;
      return;
    }
  });
  return isCheck;
}

// Function get email from sheet QR code ( Sheet 2)
function getAllColumnA1FromSheet2(){
  var sheet = SpreadsheetApp.openById(ssid).getSheets()[1];
  var column = getColumn(sheet,1);
  var data = JSON.stringify(column);
  Logger.log(data);
  return column;
}

// Functon get column a1 (sheet3 ) with check member checkin 
function getAllColumnA1WithScanned(){
  var sheet = SpreadsheetApp.openById(ssid).getSheets()[2];
  var column = getColumn(sheet,1);
  // var data = JSON.stringify(column);
  // Logger.log(data);
  return column;
}

function getAllColumnA2(){
  var sheet = SpreadsheetApp.open(ssis).getSheets()[0];
  var column = getColumn(sheet,2)
  return column;
}

function getColumn(activeSheet, columnIndex) {
  var lastRow = activeSheet.getDataRange().getLastRow()
  return activeSheet.getRange(1, columnIndex, lastRow, 1)
    .getValues()
    .flat()
}

function getEmailAndLinkQR(){
    var sheet = SpreadsheetApp.openById(ssid).getSheets()[1];
  var startRow = 2;
  var startColumn = 2;
  var numColums = 2;
  var numRow = sheet.getLastRow();
  var dataRange = sheet.getRange(startRow,startColumn,numRow,numColums);
  var data = JSON.stringify(dataRange.getValues());
  Logger.log(data);
}