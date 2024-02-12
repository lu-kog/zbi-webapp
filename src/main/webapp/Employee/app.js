// app.js

var applications_types = ["New Account request", "Debit card request", "Credit card request", "Loan request"]
document.addEventListener('DOMContentLoaded', getData);
	var getData = function() {
    // fetch data from the servlet
    
    sendRequest("GetPendingApplications","application/json",null,callBackfn);


	    function callBackfn(resposeTxt) {
	        var responseData = JSON.parse(resposeTxt);
	        console.log(responseData);
	
	        if(responseData.statuscode == 200){
	            createTable(responseData.data);
	        }
        
	    }
    
	}



// create table with data

function createTable(ApplicationsData){

	
    var tableBody = document.getElementById('applicationsTable').getElementsByTagName('tbody')[0];
    var totalApplicationsDiv = document.getElementById('totalApplications');

    // Empty the table first
    tableBody.innerHTML = '';

    // Populate the table with data
    ApplicationsData.forEach(function(item, index) {
        var row = tableBody.insertRow();
        row.setAttribute("id",`row-${item.application_id}`);
        var cellIndex = row.insertCell(0);
        var cellApplicationID = row.insertCell(1);
        var cellCustomerName = row.insertCell(2);
        var cellCustomerID = row.insertCell(3);
        var cellTypeOfApplication = row.insertCell(4);
        var cellDate = row.insertCell(5);
        var cellAction = row.insertCell(6);
        var cellSubmit = row.insertCell(7);

        cellIndex.innerHTML = index + 1;
        cellApplicationID.innerHTML = item.application_id;
        cellCustomerName.innerHTML = item.customerName;
        cellCustomerID.innerHTML = item.cusID;
        cellTypeOfApplication.innerHTML = applications_types[item.type-1];
        cellDate.innerHTML = item.date;
        cellAction.innerHTML = `
            <select name="action" id="action-${item.application_id}">
                <option value="approve">Approve</option>
                <option value="reject">Reject</option>
            </select>
        `;
        cellSubmit.innerHTML = `<button onclick="submitAction('${item.application_id}')">Submit</button>`;

    });

    // Update the total number of applications
    totalApplicationsDiv.innerHTML = `Total Number of Applications: ${ApplicationsData.length}`;
}


// when press submit action

function submitAction(applicationID) {
    var selectedAction = document.getElementById(`action-${applicationID}`).value;
    var reason = ''; 

    // If the action is "Reject", ask for a reason using prompt
    if (selectedAction === 'reject') {
        reason = prompt("Please enter the reason for rejection:");
        
        reqData = {};
        reqData.applicationID = applicationID;
        reqData.reason = reason;

        // send request for reject
        sendRequest("RejectApplications","application/json", reqData, actionCallback);


	    function actionCallback(resposeTxt) {
	        var responseData = JSON.parse(resposeTxt);
	        if(responseData.statuscode == 200){
                // deleted successfully!
                alert("deleted successfully");
                deleteRow(applicationID);
	        }
	    }

    }else if (selectedAction === 'approve') {
        reqData = {};
        reqData.applicationID = applicationID;

        // send request to approve
        sendRequest("ApproveApplication","application/json", reqData, actionCallback);


	    function actionCallback(resposeTxt) {
	        var responseData = JSON.parse(resposeTxt);
	        if(responseData.statuscode == 200){
                // deleted successfully!
                alert("approved!");
                deleteRow(applicationID);
	        }
	    }
        document.getElementById
    }

}

function deleteRow(appID){
    // delete a row by application ID
    var rowtoDelete = _(`#${appID}`);
    rowtoDelete.remove();
}
