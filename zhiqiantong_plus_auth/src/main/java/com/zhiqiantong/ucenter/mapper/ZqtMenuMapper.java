package com.zhiqiantong.ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiqiantong.ucenter.model.po.ZqtMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface ZqtMenuMapper extends BaseMapper<ZqtMenu> {
    @Select("SELECT	* FROM zqt_menu WHERE id IN (SELECT menu_id FROM zqt_permission WHERE role_id IN ( SELECT role_id FROM zqt_user_role WHERE user_id = #{userId} ))")
    List<ZqtMenu> selectPermissionByUserId(@Param("userId") String userId);
}
