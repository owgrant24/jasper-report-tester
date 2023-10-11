function generatePojo() {
    let report = document.getElementById("report").value;

    if (report.length === 0) {
        alert("Содержимое jrxml-файла не заполнено. Запрос не выполнен")
        return
    }

    console.log(report)
    axios.post('/v1/generate/pojo', {
        report: report
    })
        .then(response => {
            console.log(response)
            document.getElementById("output").value = response.data
        })
        .catch((err) => console.log(err.message));
}

function generateTestData() {
    let report = document.getElementById("report").value;

    if (report.length === 0) {
        alert("Содержимое jrxml-файла не заполнено. Запрос не выполнен")
        return
    }

    console.log(report)
    axios.post('/v1/generate/testData', {
        report: report
    })
        .then(response => {
            console.log(response.data)
            document.getElementById("output").value = JSON.stringify(response.data, null, 4)
        })
        .catch((err) => console.log(err.message));
}
