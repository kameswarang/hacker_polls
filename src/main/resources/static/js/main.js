(function() {
  'use strict';
  window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }, false);
})();

$("#skillsN").bind("change", function(e) {
	var n = e.target.value;
	var href = window.location.href;
	window.location = href.substring(0, href.length-1) + n;
});

$(".float-right").each(function(i, el) {
	$(`#${el.id}Skills`).toggle();
}).bind("click", function(e) {
	$(`#${e.target.id}Skills`).toggle();
});

function warnDelete(event) {
	var deleteProfileName = event.target.id;
	document.getElementById("deleteProfileName").innerText = deleteProfileName;
	
	$("#warningModal").modal('show');
}

function deleteProfile() {
	var profile = document.getElementById("deleteProfileName").innerText;
	axios.get("/csrf-token").then(function(response) {
		axios({
			method: "POST",
			url: "/admin/profile/delete",
			data: {
				candidate: profile
			},
			headers: {
				[response.data.headerName]: response.data.token
			}
		}).then(function() {
			window.location.reload();
		})
	})
}