// node request_generator.js http://payment.service/payroll 1000 payroll.json

const http = require('http');
const fs = require('fs');
var path = require('path');
const delay = ms => new Promise(res => setTimeout(res, ms));

/**
 *
 * @param {*} url
 * @param {*} payload JS Object
 * @returns JS Object parsed from json
 */
async function post(url, payload) {
    let promisedData = new Promise((resolve, reject) => {
        if (typeof payload != 'string') {
            payload = JSON.stringify(payload);
        }

        const options = {
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': payload.length,
            },
            method: 'POST',
        };

        const req = http.request(url, options, (resp) => {-
            resp.setEncoding('utf8');
            let chunks = [];

            // A chunk of data has been recieved.
            resp.on('data', (chunk) => {
                chunks.push(chunk);
            });

            // The whole response has been received. Print out the result.
            resp.on('end', () => {
                const rawResp = chunks.join('');
                if (rawResp.startsWith('{') || rawResp.startsWith('[')) {
                    resolve(JSON.parse(rawResp));
                } else {
                    resolve(rawResp);
                }
            });
        })
        .on('error', (err) => {
            reject(err);
        });
            req.write(payload);
            req.end();
    }).catch((_) => console.log("[ERROR] Application not available"));

    return promisedData;
}

function loadRequestData(file) {
    const requestData = fs.readFileSync(path.join(__dirname, file), 'utf8');
    return JSON.parse(requestData);
}
  
async function createRequest(requestUrl, seconds, file) {
    let currentDate = new Date();
    let timeFinish = new Date(currentDate.getTime() + (seconds*1000));

    while(timeFinish > new Date()) {
        const requestedData =  Object.assign({}, loadRequestData(file));
        post(requestUrl, requestedData);

        await delay(3000); // wait 3 seconds for next request
    }
 
}

if (require.main === module) {
    var cmdArgs = process.argv.slice(2);
    const REQUESTED_URL = cmdArgs[0];
    const TIME_REQUESTED = cmdArgs[1];
    const FILE_REQUESTED = cmdArgs[2];

    console.log('----------------------------------');
    console.log('| REQUEST GENERATOR');
    console.log('|');
    console.log(`| Request URL: ${REQUESTED_URL}`);
    console.log(`| Time to generate: ${TIME_REQUESTED} seconds`);
    console.log(`| File requested: ${FILE_REQUESTED}`);
    console.log('|');
    console.log('----------------------------------\n');

    createRequest(REQUESTED_URL, TIME_REQUESTED, FILE_REQUESTED);
}

