var candidates = [];
var names = [];
var votes = [];

axios.get("/status/get").then(function(response) {
	candidates = response.data;

	candidates.forEach((candidate)=> {
		names.push(candidate.firstName + " " + candidate.lastName);
		votes.push(candidate.votes);
	});

	var ctx = document.getElementById('myChart');
	new Chart(ctx, {
	    type: 'pie',
	    data: {
	        labels: names,
	        datasets: [{
	            data: votes,
	        }]
	    },
	    options: {
			title: {
				display: true,
				position: "top",
				fontSize: 20,
				padding: 10,
				text: "Status of the poll"
			},
			legend: {
				position: "right",
			},
			layout: {
				padding: 10
			},
		    plugins: {	
		      colorschemes: {
		        scheme: 'tableau.Classic20'
		      }
		    }
	    }
	});
	})
	.catch(function(error) {
		console.log(error);
	})