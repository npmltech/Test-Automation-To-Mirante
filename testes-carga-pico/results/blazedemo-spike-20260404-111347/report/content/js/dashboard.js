/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 93.07592086336354, "KoPercent": 6.924079136636467};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.30369278667022204, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.32932940309506264, 500, 1500, "https://www.blazedemo.com/reserve.php-5"], "isController": false}, {"data": [0.3711783439490446, 500, 1500, "https://www.blazedemo.com/purchase.php-4"], "isController": false}, {"data": [0.13286661753868828, 500, 1500, "https://www.blazedemo.com/reserve.php-4"], "isController": false}, {"data": [0.385828025477707, 500, 1500, "https://www.blazedemo.com/purchase.php-3"], "isController": false}, {"data": [0.4035031847133758, 500, 1500, "https://www.blazedemo.com/purchase.php-2"], "isController": false}, {"data": [0.030463669776646878, 500, 1500, "https://www.blazedemo.com/reserve.php"], "isController": false}, {"data": [0.5386942675159235, 500, 1500, "https://www.blazedemo.com/purchase.php-1"], "isController": false}, {"data": [0.3587579617834395, 500, 1500, "https://www.blazedemo.com/purchase.php-0"], "isController": false}, {"data": [0.4027777777777778, 500, 1500, "https://www.blazedemo.com/confirmation.php-3"], "isController": false}, {"data": [0.38897462277091904, 500, 1500, "https://www.blazedemo.com/confirmation.php-4"], "isController": false}, {"data": [0.06696503968924168, 500, 1500, "https://www.blazedemo.com/confirmation.php"], "isController": false}, {"data": [0.5542695473251029, 500, 1500, "https://www.blazedemo.com/confirmation.php-1"], "isController": false}, {"data": [0.4253257887517147, 500, 1500, "https://www.blazedemo.com/confirmation.php-2"], "isController": false}, {"data": [0.3816015089163237, 500, 1500, "https://www.blazedemo.com/confirmation.php-5"], "isController": false}, {"data": [0.05908523259444271, 500, 1500, "https://www.blazedemo.com/purchase.php"], "isController": false}, {"data": [0.4140946502057613, 500, 1500, "https://www.blazedemo.com/confirmation.php-0"], "isController": false}, {"data": [3.3478406427854036E-4, 500, 1500, "Test"], "isController": true}, {"data": [0.3341930729550479, 500, 1500, "https://www.blazedemo.com/reserve.php-1"], "isController": false}, {"data": [0.2975681650700074, 500, 1500, "https://www.blazedemo.com/reserve.php-0"], "isController": false}, {"data": [0.23537214443625645, 500, 1500, "https://www.blazedemo.com/reserve.php-3"], "isController": false}, {"data": [0.2834193072955048, 500, 1500, "https://www.blazedemo.com/reserve.php-2"], "isController": false}, {"data": [0.3677547770700637, 500, 1500, "https://www.blazedemo.com/purchase.php-5"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 132783, 9194, 6.924079136636467, 3791.1829300437676, 13, 75926, 1452.0, 4084.9000000000015, 5722.950000000001, 9510.910000000014, 707.9305840641911, 22164.063827664744, 1090.715947638152], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["https://www.blazedemo.com/reserve.php-5", 6785, 578, 8.518791451731762, 3582.2614591009656, 55, 62257, 1481.0, 10890.200000000004, 16068.199999999999, 29018.520000000153, 39.40918172947354, 153.4998927283235, 32.24963680097347], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-4", 6280, 526, 8.375796178343949, 3548.553503184717, 158, 62295, 1267.5, 11468.800000000001, 17101.85, 30852.1199999997, 39.959277169763304, 419.619343195231, 35.54580622454823], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-4", 6785, 560, 8.253500368459838, 5639.8725128960905, 144, 66665, 3405.0, 13280.20000000001, 18587.899999999994, 32707.660000000007, 37.55756797449296, 4275.26325715379, 30.75623742430365], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-3", 6280, 490, 7.802547770700637, 3208.763375796177, 158, 62621, 1191.5, 9784.600000000002, 16017.699999999999, 29748.219999999863, 39.96003996003996, 129.7538610374498, 35.7992239150181], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-2", 6280, 401, 6.385350318471337, 2809.4383757961846, 158, 54377, 1095.5, 7177.500000000025, 14343.899999999992, 22968.469999999754, 39.96588898647015, 86.30945810979802, 36.31968027686561], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php", 7074, 1316, 18.603336160588068, 11311.986994628245, 504, 75074, 7880.0, 23894.0, 32893.0, 51296.0, 37.71492549249594, 9760.258553076601, 186.05491762855544], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-1", 6280, 0, 0.0, 1183.046974522297, 13, 51575, 728.0, 2688.4000000000033, 3413.699999999999, 6571.519999999997, 40.00050956063134, 107.57817053202908, 38.1454525893642], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-0", 6280, 0, 0.0, 1758.001273885349, 235, 27260, 1283.0, 3072.600000000002, 3851.8499999999995, 12259.24999999997, 39.59022852639874, 259.7726802600473, 38.62367021276596], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-3", 5832, 351, 6.018518518518518, 2630.2381687242814, 148, 53392, 1111.5, 5888.2999999999965, 13031.399999999965, 21774.83, 38.13958355132364, 63.05282255889008, 34.964043861829026], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-4", 5832, 417, 7.150205761316872, 2902.9711934156417, 150, 62310, 1147.5, 8134.199999999993, 15211.699999999999, 22304.67, 38.14407367195574, 169.9003118582155, 34.54787048134655], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php", 5921, 1026, 17.328154028035804, 6895.655463604115, 425, 75926, 3440.0, 18436.4, 21488.39999999998, 36294.7599999999, 38.2834826912881, 552.0231213872833, 212.52448509693718], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-1", 5832, 0, 0.0, 1040.7494855967125, 13, 44176, 726.0, 2352.0, 3019.699999999999, 4135.35, 38.1797827837461, 43.45163222131115, 36.44453688110061], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-2", 5832, 285, 4.886831275720165, 2248.6181412894366, 158, 41050, 1027.0, 4054.3999999999996, 11345.099999999997, 19581.04, 38.13958355132364, 44.20778857897026, 35.328319709620565], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-5", 5832, 464, 7.956104252400548, 3075.70010288065, 152, 62281, 1201.0, 9461.999999999995, 15401.64999999998, 22741.10000000001, 38.13958355132364, 20.817816425869125, 34.233959421922414], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php", 6406, 1263, 19.715891351857632, 8228.49781454884, 413, 75250, 3820.0, 20120.300000000003, 28556.099999999995, 50931.770000000026, 40.310097031173314, 1027.111306518928, 217.23917882374872], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-0", 5832, 0, 0.0, 1448.1587791495153, 240, 14969, 1137.0, 2693.0, 3476.699999999999, 6611.4000000000015, 37.75954833571812, 213.13560813785602, 39.08703245689571], "isController": false}, {"data": ["Test", 5974, 2221, 37.17777033813191, 26551.31904921319, 1393, 105405, 19495.0, 57582.0, 67777.75, 83952.25, 33.23856207332068, 9882.943272689465, 525.5292653500231], "isController": true}, {"data": ["https://www.blazedemo.com/reserve.php-1", 6785, 1, 0.014738393515106854, 2746.8654384672113, 27, 47675, 1679.0, 5544.200000000002, 7435.699999999998, 24176.0, 38.68896580431423, 3172.5674860689787, 35.056707703010154], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-0", 6785, 0, 0.0, 2520.0058953574126, 236, 33697, 1540.0, 5364.0, 9601.699999999997, 14430.820000000022, 37.91244098007991, 272.6816183070014, 35.32077020995725], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-3", 6785, 506, 7.4576271186440675, 4251.118201915976, 147, 62076, 1993.0, 11308.400000000001, 17775.699999999997, 32571.040000000077, 38.5662560890349, 1385.0751002434902, 31.891081633211314], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-2", 6785, 382, 5.630066322770818, 3500.922770817978, 152, 54316, 1739.0, 9519.0, 15817.4, 26903.56000000002, 38.387553041018386, 1031.4258199257426, 32.299433124115986], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-5", 6280, 628, 10.0, 3591.0501592356554, 158, 55607, 1312.5, 11677.700000000019, 17072.699999999997, 30990.389999999967, 39.96309141238983, 30.90280125441471, 34.924701310891216], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Premature end of Content-Length delimited message body (expected: 28,663; received: 9,600)", 1, 0.010876658690450293, 7.53108455148626E-4], "isController": false}, {"data": ["Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 747, 8.124864041766369, 0.5625720159960236], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 132, 1.4357189471394387, 0.09941031607961863], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 151, 1.6423754622579942, 0.11371937672744252], "isController": false}, {"data": ["Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Premature end of Content-Length delimited message body (expected: 39,170; received: 36,864)", 1, 0.010876658690450293, 7.53108455148626E-4], "isController": false}, {"data": ["Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 5060, 55.035892973678486, 3.8107287830520473], "isController": false}, {"data": ["Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Connection is closed", 1, 0.010876658690450293, 7.53108455148626E-4], "isController": false}, {"data": ["Assertion failed", 3101, 33.728518599086364, 2.335389319415889], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 132783, 9194, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 5060, "Assertion failed", 3101, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 747, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 151, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 132], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["https://www.blazedemo.com/reserve.php-5", 6785, 578, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 459, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 80, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 30, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 9, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-4", 6280, 526, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 448, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 39, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 23, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 16, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-4", 6785, 560, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 454, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 79, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 19, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 8, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-3", 6280, 490, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 434, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 31, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 14, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 11, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-2", 6280, 401, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 344, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 44, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 7, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 6, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php", 7074, 1316, "Assertion failed", 1027, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 148, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 135, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 6, "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-3", 5832, 351, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 312, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 24, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 13, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 2, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-4", 5832, 417, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 376, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 23, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 18, "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php", 5921, 1026, "Assertion failed", 937, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 76, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 13, "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-2", 5832, 285, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 255, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 21, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 9, "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-5", 5832, 464, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 421, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 27, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 14, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 2, "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php", 6406, 1263, "Assertion failed", 1137, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 110, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 15, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 1, "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-1", 6785, 1, "Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Connection is closed", 1, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-3", 6785, 506, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 401, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 80, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 18, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 6, "Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Premature end of Content-Length delimited message body (expected: 39,170; received: 36,864)", 1], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-2", 6785, 382, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 295, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 73, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 10, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 3, "Non HTTP response code: org.apache.http.ConnectionClosedException/Non HTTP response message: Premature end of Content-Length delimited message body (expected: 28,663; received: 9,600)", 1], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-5", 6280, 628, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 540, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 50, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 23, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 15, "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
