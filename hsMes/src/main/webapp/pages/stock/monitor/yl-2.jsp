<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="monitor.common.js.jsp" %>
    <style type="text/css">
        html,
        body {
            margin: 0px;
            height: 100%;
        }

        th, td {
            border: solid 1px #d28080;
        }

        .dianti {
            background: #498c34;
            color: white;
            font-weight: bold;
            text-align: center;
            vertical-align: middle;
        }

        .louti {
            color: white;
            font-weight: bold;
            text-align: center;
            vertical-align: middle;
            background: linear-gradient(45deg, #fb3 25%, #58a 0, #58a 50%, #fb3 0, #fb3 75%, #58a 0);
            background-size: 40px 40px;
        }

        .louti_text {
            background: white;
            color: black;
            padding: 10px;
        }

        .bangongshi {
            background: #697bf5;
            color: white;
            font-weight: bold;
            text-align: center;
            vertical-align: middle;
        }

        .tongdao {
            background: #d28080;
            color: white;
            font-size: 36px;
            font-weight: bold;
            vertical-align: middle;
            text-align: center;
            width: 100px;
        }

        .kuwei {
            width: 100px;
            text-align: center;
            font-size: 12px;
            vertical-align: middle;
        }

        .kuwei_verticla {
            text-align: center;
            vertical-align: top;
        }

        table {
            height: 100%;
            border: none;
            border-color: #d28080;
        }

        table table {
            border: 1px solid #d28080;
            height: 100%;
        }

        td {
            min-height: 50px;
            vertical-align: top;
        }
    </style>
    <title></title>
</head>
<body type="yl">
<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080" style="BORDER-COLLAPSE: collapse">
    <tr>
        <td height="30" class="louti"><label class="louti_text">楼梯</label></td>
        <td rowspan="12" class="tongdao" style="width:10px">
            <p>通<br>
                道 </p>
            <p>|</p>
            <p>原</p>
            <p>料</p>
            <p>二</p>
            <p>号</p>
            <p>仓</p>
            <p>库</p></td>
        <td rowspan="2" class="bangongshi">办公室</td>
        <td rowspan="2" class="bangongshi">研发仓库</td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-001"></td>
                    <td class="kuwei">2-2-001</td>
                </tr>
                <tr>
                    <td kuwei="2-2-002"></td>
                    <td class="kuwei">2-2-002</td>
                </tr>
                <tr>
                    <td kuwei="2-2-003"></td>
                    <td class="kuwei">2-2-003</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-004"></td>
                    <td class="kuwei">2-2-004</td>
                </tr>
                <tr>
                    <td kuwei="2-2-005"></td>
                    <td class="kuwei">2-2-005</td>
                </tr>
                <tr>
                    <td kuwei="2-2-006"></td>
                    <td class="kuwei">2-2-006</td>
                </tr>
                <tr>
                    <td kuwei="2-2-007"></td>
                    <td class="kuwei">2-2-007</td>
                </tr>
                <tr>
                    <td kuwei="2-2-008"></td>
                    <td class="kuwei">2-2-008</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" height="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-001</td>
                    <td kuwei="2-1-001"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-002</td>
                    <td kuwei="2-1-002"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="kuwei">2-1-003</td>
        <td kuwei="2-1-003"></td>
    </tr>
    <tr>
        <td class="kuwei">2-1-004</td>
        <td kuwei="2-1-004"></td>
    </tr>
</table>
<table>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-009"></td>
                    <td class="kuwei">2-2-009</td>
                </tr>
                <tr>
                    <td kuwei="2-2-010"></td>
                    <td class="kuwei">2-2-010</td>
                </tr>
                <tr>
                    <td kuwei="2-2-011"></td>
                    <td class="kuwei">2-2-011</td>
                </tr>
                <tr>
                    <td kuwei="2-2-012"></td>
                    <td class="kuwei">2-2-012</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-005</td>
                    <td kuwei="2-1-005"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-006</td>
                    <td kuwei="2-1-006"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-007</td>
                    <td kuwei="2-1-007"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-008</td>
                    <td kuwei="2-1-008"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-013"></td>
                    <td class="kuwei">2-2-013</td>
                </tr>
                <tr>
                    <td kuwei="2-2-014"></td>
                    <td class="kuwei">2-2-014</td>
                </tr>
                <tr>
                    <td kuwei="2-2-015"></td>
                    <td class="kuwei">2-2-015</td>
                </tr>
                <tr>
                    <td kuwei="2-2-016"></td>
                    <td class="kuwei">2-2-016</td>
                </tr>
                <tr>
                    <td kuwei="2-2-017"></td>
                    <td class="kuwei">2-2-017</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-009</td>
                    <td kuwei="2-1-009"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-010</td>
                    <td kuwei="2-1-010"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-011</td>
                    <td kuwei="2-1-011"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-012</td>
                    <td kuwei="2-1-012"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-013</td>
                    <td kuwei="2-1-013"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-018"></td>
                    <td class="kuwei">2-2-018</td>
                </tr>
                <tr>
                    <td kuwei="2-2-019"></td>
                    <td class="kuwei">2-2-019</td>
                </tr>
                <tr>
                    <td kuwei="2-2-020"></td>
                    <td class="kuwei">2-2-020</td>
                </tr>
                <tr>
                    <td kuwei="2-2-021"></td>
                    <td class="kuwei">2-2-021</td>
                </tr>
                <tr>
                    <td kuwei="2-2-022"></td>
                    <td class="kuwei">2-2-022</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-014</td>
                    <td kuwei="2-1-014"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-015</td>
                    <td kuwei="2-1-015"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-016</td>
                    <td kuwei="2-1-016"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-017</td>
                    <td kuwei="2-1-017"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-018</td>
                    <td kuwei="2-1-018"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-023"></td>
                    <td class="kuwei">2-2-023</td>
                </tr>
            </table>
        </td>
        <td colspan="2" rowspan="3">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-019</td>
                    <td kuwei="2-1-019"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-020</td>
                    <td kuwei="2-1-020"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-021</td>
                    <td kuwei="2-1-021"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-022</td>
                    <td kuwei="2-1-022"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-023</td>
                    <td kuwei="2-1-023"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="tongdao">通道</td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-024"></td>
                    <td class="kuwei">2-2-024</td>
                </tr>
                <tr>
                    <td kuwei="2-2-025"></td>
                    <td class="kuwei">2-2-025</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-026"></td>
                    <td class="kuwei">2-2-026</td>
                </tr>
                <tr>
                    <td kuwei="2-2-027"></td>
                    <td class="kuwei">2-2-027</td>
                </tr>
                <tr>
                    <td kuwei="2-2-028"></td>
                    <td class="kuwei">2-2-028</td>
                </tr>
                <tr>
                    <td kuwei="2-2-029"></td>
                    <td class="kuwei">2-2-029</td>
                </tr>
                <tr>
                    <td kuwei="2-2-030"></td>
                    <td class="kuwei">2-2-030</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-024</td>
                    <td kuwei="2-1-024"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-025</td>
                    <td kuwei="2-1-025"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-026</td>
                    <td kuwei="2-1-026"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-027</td>
                    <td kuwei="2-1-027"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-028</td>
                    <td kuwei="2-1-028"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-031"></td>
                    <td class="kuwei">2-2-031</td>
                </tr>
                <tr>
                    <td kuwei="2-2-032"></td>
                    <td class="kuwei">2-2-032</td>
                </tr>
                <tr>
                    <td kuwei="2-2-033"></td>
                    <td class="kuwei">2-2-033</td>
                </tr>
                <tr>
                    <td kuwei="2-2-034"></td>
                    <td class="kuwei">2-2-034</td>
                </tr>
                <tr>
                    <td kuwei="2-2-035"></td>
                    <td class="kuwei">2-2-035</td>
                </tr>
            </table>
        </td>
        <td colspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei">2-1-029</td>
                    <td kuwei="2-1-029"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-030</td>
                    <td kuwei="2-1-030"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-031</td>
                    <td kuwei="2-1-031"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-032</td>
                    <td kuwei="2-1-032"></td>
                </tr>
                <tr>
                    <td class="kuwei">2-1-033</td>
                    <td kuwei="2-1-033"></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td rowspan="2" style="border: none;">
            <table width="100%" height="50px" border="none" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td kuwei="2-2-036"></td>
                    <td class="kuwei" height="25px">2-2-036</td>
                </tr>
                <tr>
                    <td kuwei="2-2-037"></td>
                    <td class="kuwei" height="25px">2-2-037</td>
                </tr>
                <tr>
                    <td style="border:none;"></td>
                    <td style="border:none;"></td>
                </tr>
            </table>
        </td>
        <td class="tongdao"></td>
        <td class="tongdao">通道</td>
    </tr>
    <tr>
        <td colspan="2" rowspan="2">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor=" #d28080"
                   style="BORDER-COLLAPSE: collapse">
                <tr>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>38</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>39</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>40</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>41</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>42</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>43</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>44</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>45</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>46</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>47</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>48</td>
                    <td class="kuwei_verticla">2<br>|<br>2<br>|<br>49</td>
                </tr>
                <tr>
                    <td kuwei="2-2-38">&nbsp;</td>
                    <td kuwei="2-2-39">&nbsp;</td>
                    <td kuwei="2-2-40">&nbsp;</td>
                    <td kuwei="2-2-41">&nbsp;</td>
                    <td kuwei="2-2-42">&nbsp;</td>
                    <td kuwei="2-2-43">&nbsp;</td>
                    <td kuwei="2-2-44">&nbsp;</td>
                    <td kuwei="2-2-45">&nbsp;</td>
                    <td kuwei="2-2-46">&nbsp;</td>
                    <td kuwei="2-2-47">&nbsp;</td>
                    <td kuwei="2-2-48">&nbsp;</td>
                    <td kuwei="2-2-49">&nbsp;</td>
                </tr>
            </table>
        </td>
        <td class="louti"><label class="louti_text">楼梯</label></td>
    </tr>
    <tr>
        <td style="border:none;"></td>
        <td class="dianti">电梯</td>
    </tr>
</table>
</body>
<script type="text/javascript">
    function log() {
        console.log(arguments);
    }
</script>
</html>