import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.SpuInfoDescDao;
import com.vivi.gulimall.product.entity.SpuInfoDescEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SpuInfoDescServiceImplTest {

    @Mock
    private SpuInfoDescDao spuInfoDescDao;

    @InjectMocks
    private SpuInfoDescServiceImpl spuInfoDescService;

    private SpuInfoDescEntity spuInfoDesc;

    @Before
    public void setUp() {
        spuInfoDesc = new SpuInfoDescEntity();
        spuInfoDesc.setSpuId(1L);
        spuInfoDesc.setDescript("Test Description");
    }

    @Test
    public void testQueryPage() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");

        IPage<SpuInfoDescEntity> page = new Page<>();
        when(spuInfoDescDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = spuInfoDescService.queryPage(params);

        assertNotNull(result);
        verify(spuInfoDescDao).selectPage(any(), any());
    }

    @Test
    public void testGetBySpuId() {
        when(spuInfoDescDao.selectOne(any())).thenReturn(spuInfoDesc);

        SpuInfoDescEntity result = spuInfoDescService.getBySpuId(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getSpuId());
        assertEquals("Test Description", result.getDescript());
        verify(spuInfoDescDao).selectOne(any());
    }

    @Test
    public void testGetBySpuIdNotFound() {
        when(spuInfoDescDao.selectOne(any())).thenReturn(null);

        SpuInfoDescEntity result = spuInfoDescService.getBySpuId(999L);

        assertNull(result);
        verify(spuInfoDescDao).selectOne(any());
    }
}
