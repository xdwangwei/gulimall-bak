import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.ProductAttrValueDao;
import com.vivi.gulimall.product.entity.ProductAttrValueEntity;
import com.vivi.gulimall.product.entity.SpuInfoEntity;
import com.vivi.gulimall.product.service.SpuInfoService;
import com.vivi.gulimall.product.vo.ItemAttrGroupWithAttrVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProductAttrValueServiceImplTest {

    @InjectMocks
    private ProductAttrValueServiceImpl productAttrValueService;

    @Mock
    private SpuInfoService spuInfoService;

    @Mock
    private ProductAttrValueDao baseMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testQueryPage() {
        Map<String, Object> params = new HashMap<>();
        IPage<ProductAttrValueEntity> page = mock(IPage.class);
        when(productAttrValueService.page(any(), any())).thenReturn(page);

        PageUtils result = productAttrValueService.queryPage(params);

        assertNotNull(result);
        verify(productAttrValueService).page(any(), any());
    }

    @Test
    public void testListForSpu() {
        Long spuId = 1L;
        List<ProductAttrValueEntity> expectedList = new ArrayList<>();
        when(productAttrValueService.list(any())).thenReturn(expectedList);

        List<ProductAttrValueEntity> result = productAttrValueService.listForSpu(spuId);

        assertEquals(expectedList, result);
        verify(productAttrValueService).list(any(QueryWrapper.class));
    }

    @Test
    public void testUpdateForSpu() {
        Long spuId = 1L;
        List<ProductAttrValueEntity> list = new ArrayList<>();
        ProductAttrValueEntity entity = new ProductAttrValueEntity();
        list.add(entity);

        when(productAttrValueService.remove(any())).thenReturn(true);
        when(productAttrValueService.saveBatch(any())).thenReturn(true);

        boolean result = productAttrValueService.updateForSpu(spuId, list);

        assertTrue(result);
        verify(productAttrValueService).remove(any(QueryWrapper.class));
        verify(productAttrValueService).saveBatch(any());
    }

    @Test
    public void testGetAttrsWithAttrGroupBySpuId() {
        Long spuId = 1L;
        SpuInfoEntity spuInfo = new SpuInfoEntity();
        spuInfo.setCatelogId(1L);
        List<ItemAttrGroupWithAttrVO> expectedList = new ArrayList<>();

        when(spuInfoService.getById(spuId)).thenReturn(spuInfo);
        when(baseMapper.getAttrsWithAttrGroup(eq(spuId), eq(spuInfo.getCatelogId()))).thenReturn(expectedList);

        List<ItemAttrGroupWithAttrVO> result = productAttrValueService.getAttrsWithAttrGroupBySpuId(spuId);

        assertEquals(expectedList, result);
        verify(spuInfoService).getById(spuId);
        verify(baseMapper).getAttrsWithAttrGroup(spuId, spuInfo.getCatelogId());
    }
}
