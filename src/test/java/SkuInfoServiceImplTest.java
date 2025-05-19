import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.common.utils.R;
import com.vivi.gulimall.product.dao.SkuInfoDao;
import com.vivi.gulimall.product.entity.SkuImagesEntity;
import com.vivi.gulimall.product.entity.SkuInfoEntity;
import com.vivi.gulimall.product.entity.SpuInfoDescEntity;
import com.vivi.gulimall.product.feign.SeckillFeignService;
import com.vivi.gulimall.product.feign.WareFeignService;
import com.vivi.gulimall.product.service.*;
import com.vivi.gulimall.product.vo.ItemAttrGroupWithAttrVO;
import com.vivi.gulimall.product.vo.ItemDetailVO;
import com.vivi.gulimall.product.vo.ItemSaleAttrVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RunWith(MockitoJUnitRunner.class)
public class SkuInfoServiceImplTest {

    @InjectMocks
    private SkuInfoServiceImpl skuInfoService;

    @Mock
    private SkuInfoDao skuInfoDao;

    @Mock
    private ThreadPoolExecutor executor;

    @Mock
    private SkuImagesService skuImagesService;

    @Mock
    private SpuInfoDescService spuInfoDescService;

    @Mock
    private SkuSaleAttrValueService skuSaleAttrService;

    @Mock
    private ProductAttrValueService productAttrValueService;

    @Mock
    private WareFeignService wareFeignService;

    @Mock
    private SeckillFeignService seckillFeignService;

    private SkuInfoEntity skuInfo;
    private List<SkuInfoEntity> skuInfoList;
    private Map<String, Object> params;

    @Before
    public void setUp() {
        skuInfo = new SkuInfoEntity();
        skuInfo.setSkuId(1L);
        skuInfo.setSpuId(1L);
        skuInfo.setPrice(new BigDecimal("99.99"));
        skuInfo.setCatelogId(10L);
        skuInfo.setBrandId(20L);
        skuInfo.setSkuName("Test SKU");

        skuInfoList = new ArrayList<>();
        skuInfoList.add(skuInfo);

        params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
    }

    @Test
    public void testQueryPage() {
        IPage<SkuInfoEntity> page = mock(IPage.class);
        when(page.getRecords()).thenReturn(skuInfoList);
        when(page.getTotal()).thenReturn(1L);
        when(skuInfoDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuInfoService.queryPage(params);

        assertEquals(1, result.getTotalCount());
        assertEquals(skuInfoList, result.getList());
        verify(skuInfoDao).selectPage(any(), any());
    }

    @Test
    public void testQueryPageConditionWithAllParams() {
        params.put("key", "test");
        params.put("catelogId", "10");
        params.put("brandId", "20");
        params.put("min", "10.00");
        params.put("max", "100.00");

        IPage<SkuInfoEntity> page = mock(IPage.class);
        when(page.getRecords()).thenReturn(skuInfoList);
        when(page.getTotal()).thenReturn(1L);
        when(skuInfoDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuInfoService.queryPageCondition(params);

        assertEquals(1, result.getTotalCount());
        assertEquals(skuInfoList, result.getList());
        verify(skuInfoDao).selectPage(any(), any());
    }

    @Test
    public void testQueryPageConditionWithEmptyParams() {
        IPage<SkuInfoEntity> page = mock(IPage.class);
        when(page.getRecords()).thenReturn(skuInfoList);
        when(page.getTotal()).thenReturn(1L);
        when(skuInfoDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuInfoService.queryPageCondition(params);

        assertEquals(1, result.getTotalCount());
        assertEquals(skuInfoList, result.getList());
        verify(skuInfoDao).selectPage(any(), any());
    }

    @Test
    public void testQueryPageConditionWithInvalidPriceRange() {
        params.put("min", "invalid");
        params.put("max", "invalid");

        IPage<SkuInfoEntity> page = mock(IPage.class);
        when(page.getRecords()).thenReturn(skuInfoList);
        when(page.getTotal()).thenReturn(1L);
        when(skuInfoDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = skuInfoService.queryPageCondition(params);

        assertEquals(1, result.getTotalCount());
        assertEquals(skuInfoList, result.getList());
        verify(skuInfoDao).selectPage(any(), any());
    }

    @Test
    public void testListBySpuId() {
        when(skuInfoDao.selectList(any(QueryWrapper.class))).thenReturn(skuInfoList);

        List<SkuInfoEntity> result = skuInfoService.listBySpuId(1L);

        assertEquals(skuInfoList, result);
        verify(skuInfoDao).selectList(any(QueryWrapper.class));
    }

    @Test
    public void testDetail() {
        when(skuInfoDao.selectById(1L)).thenReturn(skuInfo);

        List<SkuImagesEntity> images = new ArrayList<>();
        when(skuImagesService.listBySkuId(1L)).thenReturn(images);

        SpuInfoDescEntity spuDesc = new SpuInfoDescEntity();
        when(spuInfoDescService.getBySpuId(1L)).thenReturn(spuDesc);

        List<ItemSaleAttrVO> saleAttrs = new ArrayList<>();
        when(skuSaleAttrService.allAttrValueWithSkuBySpuId(1L)).thenReturn(saleAttrs);

        List<ItemAttrGroupWithAttrVO> attrGroups = new ArrayList<>();
        when(productAttrValueService.getAttrsWithAttrGroupBySpuId(1L)).thenReturn(attrGroups);

        when(wareFeignService.getSkuStock(1L)).thenReturn(R.ok().setData(10L));
        when(seckillFeignService.getSkuSeckillInfo(1L)).thenReturn(R.ok());

        ItemDetailVO result = skuInfoService.detail(1L);

        assertNotNull(result);
        assertEquals(skuInfo, result.getSkuInfo());
        assertEquals(images, result.getSkuImages());
        assertEquals(spuDesc, result.getSpuDesc());
        assertEquals(saleAttrs, result.getSaleAttrs());
        assertEquals(attrGroups, result.getSpuAttrGroups());
        assertTrue(result.isHasStock());
        verify(skuInfoDao).selectById(1L);
        verify(skuImagesService).listBySkuId(1L);
        verify(spuInfoDescService).getBySpuId(1L);
        verify(skuSaleAttrService).allAttrValueWithSkuBySpuId(1L);
        verify(productAttrValueService).getAttrsWithAttrGroupBySpuId(1L);
        verify(wareFeignService).getSkuStock(1L);
        verify(seckillFeignService).getSkuSeckillInfo(1L);
    }
}
