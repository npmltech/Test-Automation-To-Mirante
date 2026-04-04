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

    var data = {"OkPercent": 99.69386976105046, "KoPercent": 0.3061302389495381};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.2892153996813657, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.32237037037037036, 500, 1500, "https://www.blazedemo.com/reserve.php-5"], "isController": false}, {"data": [0.3775359947643979, 500, 1500, "https://www.blazedemo.com/purchase.php-4"], "isController": false}, {"data": [0.15155555555555555, 500, 1500, "https://www.blazedemo.com/reserve.php-4"], "isController": false}, {"data": [0.38162630890052357, 500, 1500, "https://www.blazedemo.com/purchase.php-3"], "isController": false}, {"data": [0.38964332460732987, 500, 1500, "https://www.blazedemo.com/purchase.php-2"], "isController": false}, {"data": [0.05370016183610416, 500, 1500, "https://www.blazedemo.com/reserve.php"], "isController": false}, {"data": [0.46793193717277487, 500, 1500, "https://www.blazedemo.com/purchase.php-1"], "isController": false}, {"data": [0.33532395287958117, 500, 1500, "https://www.blazedemo.com/purchase.php-0"], "isController": false}, {"data": [0.3643052703627652, 500, 1500, "https://www.blazedemo.com/confirmation.php-3"], "isController": false}, {"data": [0.35797399041752226, 500, 1500, "https://www.blazedemo.com/confirmation.php-4"], "isController": false}, {"data": [0.09693702943189596, 500, 1500, "https://www.blazedemo.com/confirmation.php"], "isController": false}, {"data": [0.44686858316221767, 500, 1500, "https://www.blazedemo.com/confirmation.php-1"], "isController": false}, {"data": [0.36661533196440793, 500, 1500, "https://www.blazedemo.com/confirmation.php-2"], "isController": false}, {"data": [0.3544661190965092, 500, 1500, "https://www.blazedemo.com/confirmation.php-5"], "isController": false}, {"data": [0.10976607230492394, 500, 1500, "https://www.blazedemo.com/purchase.php"], "isController": false}, {"data": [0.324435318275154, 500, 1500, "https://www.blazedemo.com/confirmation.php-0"], "isController": false}, {"data": [0.0, 500, 1500, "Test"], "isController": true}, {"data": [0.32955555555555555, 500, 1500, "https://www.blazedemo.com/reserve.php-1"], "isController": false}, {"data": [0.27955555555555556, 500, 1500, "https://www.blazedemo.com/reserve.php-0"], "isController": false}, {"data": [0.2454074074074074, 500, 1500, "https://www.blazedemo.com/reserve.php-3"], "isController": false}, {"data": [0.2745185185185185, 500, 1500, "https://www.blazedemo.com/reserve.php-2"], "isController": false}, {"data": [0.37279123036649214, 500, 1500, "https://www.blazedemo.com/purchase.php-5"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 130990, 401, 0.3061302389495381, 2602.5269562561966, 15, 52296, 2506.5, 9728.600000000006, 13271.900000000001, 29707.56000000007, 906.3609252506521, 27863.998169300907, 1473.9934614646113], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["https://www.blazedemo.com/reserve.php-5", 6750, 30, 0.4444444444444444, 2186.7697777777767, 161, 23428, 1600.0, 6281.300000000004, 7881.349999999999, 10883.499999999989, 52.83964147324748, 209.5378763601315, 47.056636267564286], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-4", 6112, 11, 0.1799738219895288, 1834.245091623034, 158, 22599, 1442.5, 2828.7, 7314.749999999997, 9914.789999999997, 50.63039480442022, 91.94540038560943, 49.39580585020461], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-4", 6750, 38, 0.562962962962963, 4837.913777777781, 229, 45051, 3515.0, 10766.10000000001, 14741.05, 24291.64999999998, 48.24702476680605, 5939.910236242897, 42.821803187877485], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-3", 6112, 12, 0.19633507853403143, 1765.3118455497397, 159, 21494, 1415.0, 2685.199999999999, 7223.049999999998, 9397.319999999996, 50.653472895585224, 32.832710602048685, 49.41509196843689], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-2", 6112, 2, 0.032722513089005235, 1633.6922447644026, 159, 14689, 1368.0, 2490.0, 5824.699999999999, 8475.489999999998, 50.68960083598033, 25.30637029398227, 49.435337157480284], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php", 6797, 145, 2.1332941003383845, 7981.952331911155, 522, 52296, 6265.0, 17124.799999999996, 22486.29999999999, 33893.11999999992, 47.03057644803942, 13211.831172633421, 251.90471683140402], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-1", 6112, 0, 0.0, 1216.4887107329853, 15, 10777, 1143.5, 2287.7, 2876.3499999999995, 4224.529999999998, 50.975813177648035, 44.3609339162844, 48.66715101125938], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-0", 6112, 0, 0.0, 1531.7755235602071, 232, 13662, 1519.5, 2484.0999999999995, 2692.699999999999, 7571.399999999998, 51.271296630287985, 336.41827326376364, 50.01955598990009], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-3", 5844, 8, 0.13689253935660506, 1634.2973990417488, 159, 13509, 1446.0, 2431.0, 4963.5, 8453.70000000001, 47.97675048641726, 9.648013231780903, 46.87894444376031], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-4", 5844, 10, 0.17111567419575632, 1720.6697467488073, 159, 14491, 1461.5, 2518.0, 6540.0, 8828.500000000005, 47.97793212157037, 11.549190862416467, 46.864705710309835], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php", 5844, 30, 0.5133470225872689, 4031.4936687200607, 430, 23746, 3746.0, 8599.5, 10238.0, 13735.650000000016, 47.62836185819071, 319.9346570649959, 280.80799205378975], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-1", 5844, 0, 0.0, 1208.4200889801534, 15, 7311, 1230.0, 2172.5, 2608.75, 4025.750000000001, 48.03629847605583, 12.915664775949793, 45.878016524807244], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-2", 5844, 6, 0.1026694045174538, 1615.9419917864436, 157, 14316, 1436.5, 2416.0, 4684.75, 8443.600000000002, 47.97202452779078, 8.817320212648065, 46.79818711983156], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-5", 5844, 14, 0.23956194387405885, 1798.1394592744691, 159, 23378, 1479.0, 2721.0, 7222.75, 9095.500000000002, 47.79898905628895, 8.521754255206034, 46.658635757635246], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php", 6113, 30, 0.49075740225748404, 4199.502699165703, 426, 31894, 3717.0, 9448.000000000002, 10567.3, 15840.699999999968, 49.59757245318534, 526.456197845674, 289.25950491675593], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-0", 5844, 0, 0.0, 1523.0727241615368, 243, 14198, 1546.0, 2481.5, 2660.0, 3690.650000000004, 47.86946478596353, 270.20110586532, 49.55237565734507], "isController": false}, {"data": ["Test", 5844, 193, 3.302532511978097, 14935.092573579721, 1701, 48719, 14365.5, 26426.0, 30698.75, 36925.500000000015, 44.97183488780127, 13398.896247200802, 767.9015040511397], "isController": true}, {"data": ["https://www.blazedemo.com/reserve.php-1", 6750, 0, 0.0, 3161.1795555555536, 24, 49203, 1844.0, 6688.700000000002, 9232.25, 28451.859999999997, 47.340847085557186, 3882.5966101213853, 42.9026426712862], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-0", 6750, 0, 0.0, 1983.5734814814816, 235, 14422, 1718.5, 2921.9000000000005, 6530.799999999999, 10189.53999999999, 52.14811610102056, 375.07030036832026, 48.583303476927355], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-3", 6750, 32, 0.4740740740740741, 3138.890518518519, 185, 33455, 2119.0, 7822.200000000004, 11172.199999999997, 18399.899999999976, 50.63993878194067, 1943.6267585656517, 45.035038265975956], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-2", 6750, 26, 0.3851851851851852, 2760.705925925928, 180, 25304, 1905.0, 6709.20000000001, 10409.8, 16898.45999999999, 50.48805116122518, 1423.2942094763268, 44.8418333239837], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-5", 6112, 7, 0.11452879581151833, 1824.2670157068098, 161, 17460, 1455.0, 3165.199999999999, 7245.749999999997, 9003.309999999998, 49.70641336347815, 10.9646981257421, 48.53157123745547], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 115, 28.678304239401495, 0.08779296129475532], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 4, 0.9975062344139651, 0.0030536682189480115], "isController": false}, {"data": ["Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 125, 31.17206982543641, 0.09542713184212535], "isController": false}, {"data": ["Assertion failed", 157, 39.15211970074813, 0.11985647759370945], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 130990, 401, "Assertion failed", 157, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 125, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 115, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 4, "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["https://www.blazedemo.com/reserve.php-5", 6750, 30, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 17, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 13, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-4", 6112, 11, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 6, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 4, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 1, "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-4", 6750, 38, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 21, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 16, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 1, "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-3", 6112, 12, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 7, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 5, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-2", 6112, 2, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php", 6797, 145, "Assertion failed", 98, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 24, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 23, "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-3", 5844, 8, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 6, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 2, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-4", 5844, 10, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 6, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 4, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php", 5844, 30, "Assertion failed", 30, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-2", 5844, 6, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 4, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 2, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/confirmation.php-5", 5844, 14, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 8, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 6, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php", 6113, 30, "Assertion failed", 29, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 1, "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-3", 6750, 32, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 15, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 15, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Broken pipe", 2, "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/reserve.php-2", 6750, 26, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 15, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 11, "", "", "", "", "", ""], "isController": false}, {"data": ["https://www.blazedemo.com/purchase.php-5", 6112, 7, "Non HTTP response code: javax.net.ssl.SSLHandshakeException/Non HTTP response message: Remote host terminated the handshake", 5, "Non HTTP response code: org.apache.http.NoHttpResponseException/Non HTTP response message: www.blazedemo.com:443 failed to respond", 2, "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
