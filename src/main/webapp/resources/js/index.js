$(document).ready(function () {
	$(".datefield").mask("99/99/9999");

});

function periods(n) {
	var date = new Date();
	var startDate;
	var endDate;
	var dayOfWeek = (date.getDay() + 6)%7; //day of week, start from monday(0) sunday(6)
	switch(n) {
		case "lquarter":
			startDate = new Date(date.getFullYear(),Math.floor(date.getMonth()/3 - 1) * 3);
			endDate = new Date(date.getFullYear(),Math.floor(date.getMonth()/3) * 3, 0);
		break;
		case "lmonth":
			startDate = new Date(date.getFullYear(),date.getMonth()-1);
			endDate = new Date(date.getFullYear(),date.getMonth(),0);
		break;
		case "lweek":
			startDate = new Date(date.getFullYear(),date.getMonth(),date.getDate() - dayOfWeek - 7);
			endDate = new Date(date.getFullYear(),date.getMonth(),date.getDate() - dayOfWeek);
		break;
		case "cquarter":
			startDate = new Date(date.getFullYear(),Math.floor(date.getMonth()/3) * 3);
			endDate = new Date(date.getFullYear(),date.getMonth(),date.getDate());
		break;
		case "cmonth":
			startDate = new Date(date.getFullYear(),date.getMonth());
			endDate = new Date(date.getFullYear(),date.getMonth(),date.getDate());
		break;
		case "cweek":
			startDate = new Date(date.getFullYear(),date.getMonth(),date.getDate() - dayOfWeek);
			endDate = new Date(date.getFullYear(),date.getMonth(),date.getDate());
		break;
	}
	// date or month "4" format make to "04"
	// else mask clear line
	var formatXX = function(n) {
		var prefix = "";
		if (Math.floor(n/10) == 0) {
			prefix = "0";
		}
		return prefix + n; 
	};
	var startText = "";
	var endText = "";
	if (n != "custom") {
		var mmFormat = formatXX(startDate.getMonth() + 1);
		var ddFormat = formatXX(startDate.getDate());
		var year = startDate.getFullYear()
		startText = ddFormat + "/" + mmFormat + "/" + year;
		mmFormat = formatXX(endDate.getMonth() + 1);
		ddFormat = formatXX(endDate.getDate());
		year = endDate.getFullYear()
		endText = ddFormat + "/" + mmFormat + "/" + year;
	}
	$("#startDate").val(startText);
	$("#endDate").val(endText);
}

function getTasks() {
	var xhr = new XMLHttpRequest();
	var form = document.forms.filter;

	var formData = {
		assignee: form.assignee.value,
		startDate: form.startDate.value,
		endDate: form.endDate.value
	};

	//validate date fields
	try {
		new Date(formData.startDate).toISOString();
		new Date(formData.endDate).toISOString();
		location.href = "/TaskTable/api/filter?assignee="+formData.assignee+"&startDate="+formData.startDate+"&endDate="+formData.endDate;
	} catch (e) {
		alert("Date is invalid");
	}
}