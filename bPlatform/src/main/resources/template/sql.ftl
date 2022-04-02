<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqls SYSTEM "../../sql-1.0.dtd">
<sqls class="${Entity}">
	<sql comment="${name}列表" id="${entity}-list">
		<![CDATA[
			select t.* from ${entity_table_name} t where 1=1
			${"<#if key[\"查询字段名\"]??>"}
			 and 查询字段名 like :查询字段名
			${"</#if>"}
			${"<#if key[\"sort\"]??&&key[\"order\"]??>"}
				${"order by"} ${"$"}${"{key[\"sort\"]}"} ${"$"}${"{key[\"order\"]}"}
			${"</#if>"}
		]]>
	</sql>
</sqls>