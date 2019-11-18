$(document).ready(function () {
	$(".datepicker").jqxDateTimeInput({theme:"arctic", width:250, height:30});
});
    

function createTask() {
    var xhr = new XMLHttpRequest();
    var form = document.forms.addtask;
    var startDate = $('#startDate').jqxDateTimeInput('val');
    var endDate = $('#endDate').jqxDateTimeInput('val');

    var formData = {
        summary: form.summary.value,
        assignee: form.assignee.value,
        startDate: startDate,
        endDate: endDate
    };

	var url = '/TaskTable/api/addTask';

    var json = JSON.stringify(formData);

    xhr.open(
        "POST",
        url,
        true
    );
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) {
            return
        }
        if (xhr.status === 200) { //get response
            console.log(xhr.responseText);
            location.href = "/TaskTable/api/filter";
        } else {
            console.log('err', xhr.responseText);
            alert(xhr.responseText);
        }
    }
}