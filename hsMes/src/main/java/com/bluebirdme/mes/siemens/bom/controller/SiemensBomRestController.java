package com.bluebirdme.mes.siemens.bom.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.siemens.bom.entity.Grid;
import com.bluebirdme.mes.siemens.bom.entity.Suit;
import com.bluebirdme.mes.siemens.bom.service.IDrawingsService;
import com.bluebirdme.mes.siemens.bom.service.IFragmentService;
import com.bluebirdme.mes.siemens.bom.service.ISiemensBomService;
import com.bluebirdme.mes.siemens.bom.service.ISuitService;
import com.bluebirdme.mes.utils.MapUtils;
import org.springframework.web.bind.annotation.*;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 西门子BOM
 *
 * @author Goofy
 * @Date 2017年7月18日 下午2:21:14
 */
@RestController
@RequestMapping("/siemens/bom")
public class SiemensBomRestController extends BaseController {
    @Resource
    IDrawingsService drawingsService;

    @Resource
    ISiemensBomService siemensBomService;

    @Resource
    IFragmentService fragmentService;

    @Resource
    ISuitService suitService;

    @Journal(name = "获取套材BOM列表")
    @RequestMapping("list")
    public String list(Boolean siemens, String code, Long tcBomId, Boolean showChildren) throws Exception {
        if (tcBomId == null) {
            List<Map<String, Object>> list = siemensBomService.findPageInfo(siemens, code);
            List<Map<String, Object>> nodes = new ArrayList<>();
            Map<String, Object> node;
            for (final Map<String, Object> map : list) {
                node = new HashMap<>();
                node.put("id", map.get("ID"));
                node.put("text", map.get("TEXT"));
                node.put("iconCls", "platform-icon75");
                if (showChildren == null || showChildren)
                    node.put("state", "closed");
                nodes.add(node);
            }
            List<Map<String, Object>> root = new ArrayList<>();
            Map<String, Object> rootNode = new HashMap<>();
            if (siemens != null && siemens) {
                rootNode.put("id", -1);
                rootNode.put("text", "西门子");
                rootNode.put("iconCls", "platform-icon76");
                rootNode.put("state", "open");
            } else {
                rootNode.put("id", -1);
                rootNode.put("text", "全部");
                rootNode.put("iconCls", "platform-icon76");
                rootNode.put("state", "open");
            }
            rootNode.put("children", nodes);
            root.add(rootNode);
            return GsonTools.toJson(root);
        } else {
            List<Map<String, Object>> list = siemensBomService.listAllParts(tcBomId);
            List<Map<String, Object>> nodes = new ArrayList<>();
            Map<String, Object> node;
            Map<String, Object> attr;
            for (final Map<String, Object> map : list) {
                node = new HashMap<>();
                node.put("id", map.get("ID"));
                node.put("text", map.get("NAME"));
                node.put("iconCls", "platform-icon65");
                attr = new HashMap<>();
                attr.put("needSort", map.get("NEEDSORT"));
                node.put("attributes", attr);
                nodes.add(node);
            }
            return GsonTools.toJson(nodes);
        }
    }

    @RequestMapping("parts")
    public String listAllParts(Long tcBomId) {
        return GsonTools.toJson(siemensBomService.listAllParts(tcBomId));
    }

    /**
     * 启用或者禁用
     *
     * @param partId        部件ID
     * @param enable        启用状态,0/1,如果不传表示返回部件状态
     * @param isDrawingsBom true:图纸BOM，false:组套BOM
     */
    @ResponseBody
    @RequestMapping("enable")
    public String enableOrDisable(@RequestParam() Long partId, Integer enable, @RequestParam(required = true) Boolean isDrawingsBom) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", siemensBomService.enableOrDisable(partId, enable, isDrawingsBom));
        return GsonTools.toJson(map);
    }

    /**
     * 组套BOM管理开始
     */
    @Journal(name = "图纸转组套BOM")
    @ResponseBody
    @RequestMapping("drawingsToSuit")
    public String drawingsToSuit(Long partId) {
        List<Drawings> drawingsList = drawingsService.find(Drawings.class, "partId", partId);
        Collections.sort(drawingsList);
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> obj;
        int i = 0;
        for (Drawings d : drawingsList) {
            obj = new HashMap<>();
            obj.put("tcBomId", d.getTcBomId());
            obj.put("fragmentId", d.getFragmentId());
            obj.put("fragmentName", d.getFragmentName());
            obj.put("fragmentCode", d.getFragmentCode());
            obj.put("fragmentWeight", d.getFragmentWeight());
            obj.put("fragmentLength", d.getFragmentLength());
            obj.put("fragmentWidth", d.getFragmentWidth());
            obj.put("fragmentMemo", d.getFragmentMemo());
            obj.put("partId", d.getPartId());
            obj.put("partName", d.getPartName());
            obj.put("suitCount", 1);
            obj.put("suitSort", 1);
            obj.put("farbicModel", d.getFarbicModel());
            obj.put("fragmentCountPerDrawings", d.getFragmentCountPerDrawings());
            obj.put("id", (++i));
            ret.add(obj);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("partId", partId);
        suitService.delete(Suit.class, map);
        return GsonTools.toJson(ret);
    }

    @Journal(name = "获取组套BOM列表")
    @RequestMapping("suit/list")
    public String suitList(Long partId) throws Exception {
        List<Map<String, Object>> list = suitService.suitList(partId);
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> obj;
        for (Map<String, Object> m : list) {
            obj = new HashMap<>();
            obj.put("tcBomId", m.get("TCBOMID"));
            obj.put("farbicModel", m.get("FARBICMODEL"));
            obj.put("fragmentId", m.get("FRAGMENTID"));
            obj.put("fragmentName", m.get("FRAGMENTNAME"));
            obj.put("fragmentCode", m.get("FRAGMENTCODE"));
            obj.put("fragmentWeight", m.get("FRAGMENTWEIGHT"));
            obj.put("fragmentLength", m.get("FRAGMENTLENGTH"));
            obj.put("fragmentWidth", m.get("FRAGMENTWIDTH"));
            obj.put("fragmentMemo", m.get("FRAGMENTMEMO"));
            obj.put("partId", m.get("PARTID"));
            obj.put("partName", m.get("PARTNAME"));
            obj.put("suitCount", m.get("SUITCOUNT"));
            obj.put("suitSort", m.get("SUITSORT"));
            obj.put("fragmentCountPerDrawings", m.get("FRAGMENTCOUNTPERDRAWINGS"));
            obj.put("id", m.get("ID"));
            ret.add(obj);
        }
        return GsonTools.toJson(ret);
    }

    @Journal(name = "保存图纸BOM", logType = LogType.DB)
    @RequestMapping(value = "suit/add", method = RequestMethod.POST)
    public String saveSuit(@RequestBody Grid<Suit> grid) {
        suitService.saveSuitGird(grid);
        return ajaxSuccess();
    }

    /**
     * 组套BOM管理开始 图纸BOM管理开始
     */
    @Journal(name = "获取图纸BOM列表")
    @RequestMapping("drawings/list")
    public String drawingsList(Long partId) {
        List<Map<String, Object>> list = drawingsService.drawingsList(partId);
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> obj;
        for (Map<String, Object> m : list) {
            obj = new HashMap<>();
            obj.put("tcBomId", m.get("TCBOMID"));
            obj.put("fragmentId", m.get("FRAGMENTID"));
            obj.put("fragmentName", m.get("FRAGMENTNAME"));
            obj.put("fragmentWeight", m.get("FRAGMENTWEIGHT"));
            obj.put("fragmentLength", m.get("FRAGMENTLENGTH"));
            obj.put("fragmentWidth", m.get("FRAGMENTWIDTH"));
            obj.put("farbicModel", m.get("FARBICMODEL"));
            obj.put("fragmentCountPerDrawings", m.get("FRAGMENTCOUNTPERDRAWINGS"));
            obj.put("fragmentDrawingNo", m.get("FRAGMENTDRAWINGNO"));
            obj.put("fragmentDrawingVer", m.get("FRAGMENTDRAWINGVER"));
            obj.put("partId", m.get("PARTID"));
            obj.put("partName", m.get("PARTNAME"));
            obj.put("suitCountPerDrawings", m.get("SUITCOUNTPERDRAWINGS"));
            obj.put("printSort", m.get("PRINTSORT"));
            obj.put("fragmentMemo", m.get("FRAGMENTMEMO"));
            obj.put("isDeleted", m.get("ISDELETED"));
            obj.put("fragmentCode", m.get("FRAGMENTCODE"));
            obj.put("id", m.get("ID"));
            ret.add(obj);
        }
        return GsonTools.toJson(ret);
    }

    @Journal(name = "保存图纸BOM", logType = LogType.DB)
    @RequestMapping(value = "drawings/add", method = RequestMethod.POST)
    public String saveDrawings(@RequestBody Grid<Drawings> grid) {
        drawingsService.saveDrawingsList(grid);
        return ajaxSuccess();
    }

    /**
     * 图纸BOM管理结束 裁片管理开始
     */
    @Journal(name = "保存裁片列表", logType = LogType.DB)
    @RequestMapping(value = "fragment/add", method = RequestMethod.POST)
    public String saveFragment(@RequestBody Grid<Fragment> grid) {
        fragmentService.saveFragmentList(grid);
        return ajaxSuccess();
    }

    @Journal(name = "裁片列表", logType = LogType.DB)
    @RequestMapping(value = "fragment/list")
    public String fragmentList(Long tcBomId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Fragment> list = fragmentService.fragmentList(tcBomId);
        List<Map<Object, Object>> ret = new ArrayList<>();
        for (Fragment fg : list) {
            ret.add(MapUtils.entityToMap(fg));
        }
        return GsonTools.toJson(ret);
    }
}
