const fs = require('fs');

const startUpdateLine = '// TODO: create action for automatic update Markdown table with tests from junit.'

console.log("Staart !")

const report = fs.readFileSync('lang/kotlin/build/reports/report.json').toString()

const reportJson = JSON.parse(report)

console.log(createTable(reportJson))

updateReadme(createProgressBar(5) + '\n\n' + createTable(reportJson))

function createProgressBar(count) {
    let snowflakes = 25 - count;
    return '## [' + '🎄'.repeat(count) + '❄️'.repeat(snowflakes) + ']';
}

function createTable(reportJson) {
    return `<table>${createHeader()}${createRowsContent(reportJson)}</table>`
}

function updateReadme(newContent) {
    const readme = fs.readFileSync('README.md').toString()
    const startIndex = readme.indexOf(startUpdateLine)
    const updatedReadme = readme.slice(0, startIndex + startUpdateLine.length) + '\n\n' + newContent
    fs.writeFileSync('README.md', updatedReadme);
}

function createRowsContent(reportJson) {
    return Object.keys(reportJson).map(rowName => {
        let row = reportJson[rowName]
        let checks = []
        let info = []
        row.forEach(test => {
            if (test.status === 'SUCCESSFUL') {
                checks.push('✅')
            } else {
                checks.push('❌')
            }
            info.push(`${test.name} ${test.status} ${test.error ? test.error : ''} `)
        })
        return createRow(rowName, checks.join(''), info.join('<br>'))
    }).join('')
}

function createHeader() {
    return `<tr><td>Test</td><td>Result</td><td>Info</td></tr>`
}

function createRow(rowName, checks, info) {
    return `<tr><td>${rowName}</td><td>${checks}</td><td>${spoiler('Info', info)}</td></tr>`
}

function spoiler(name, content) {
    return `<details><summary>${name}</summary>${content}</details>`
}