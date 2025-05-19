import com.vivi.common.to.SpuBoundsTO;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.R;
import com.vivi.gulimall.product.dao.SpuInfoDao;
import com.vivi.gulimall.product.entity.*;
import com.vivi.gulimall.product.feign.CouponFeignService;
import com.vivi.gulimall.product.feign.SearchFeignService;
import com.vivi.gulimall.product.feign.WareFeignService;
import com.vivi.gulimall.product.service.*;
import com.vivi.gulimall.product.vo.SpuVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpuInfoServiceImplTest {

    @InjectMocks
    private SpuInfoServiceImpl spuInfoService;

    @Mock
    private SpuInfoDao spuInfoDao;

    @Mock
    private SpuInfoDescService spuInfoDescService;

    @Mock
    private SpuImagesService spuImagesService;

    @Mock
    private AttrService attrService;

    @Mock
    private ProductAttrValueService productAttrValueService;

    @Mock
    private SkuInfoService skuInfoService;

    @Mock
    private SkuImagesService skuImagesService;

    @Mock
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Mock
    private CouponFeignService couponFeignService;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private WareFeignService wareFeignService;

    @Mock
    private SearchFeignService searchFeignService;

    private SpuVO spuVO;
    private SpuInfoEntity spuInfoEntity;

    @Before
    public void setUp() {
        spuVO = new SpuVO();
        spuVO.setSpuName("Test SPU");
        spuVO.setCatelogId(1L);
        spuVO.setBrandId(1L);
        spuVO.setWeight(new BigDecimal("1.0"));
        spuVO.setPublishStatus(1);

        spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setId(1L);
        spuInfoEntity.setSpuName("Test SPU");
    }

    @Test
    public void testQueryPage() {
        Map<String, Object> params = new HashMap<>();
        PageUtils result = spuInfoService.queryPage(params);
        assertNotNull(result);
    }

    @Test
    public void testSave() {
        when(spuInfoDao.insert(any())).thenReturn(1);
        when(couponFeignService.saveSpuBounds(any())).thenReturn(R.ok());

        boolean result = spuInfoService.save(spuVO);

        assertTrue(result);
        verify(spuInfoDao).insert(any());
    }

    @Test
    public void testQueryPageCondition() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "test");
        params.put("catelogId", "1");
        params.put("brandId", "1");
        params.put("status", "1");

        PageUtils result = spuInfoService.queryPageCondition(params);

        assertNotNull(result);
    }

    @Test
    public void testStatusUp() {
        List<SkuInfoEntity> skuList = new ArrayList<>();
        SkuInfoEntity sku = new SkuInfoEntity();
        sku.setSkuId(1L);
        skuList.add(sku);

        when(skuInfoService.listBySpuId(1L)).thenReturn(skuList);
        when(wareFeignService.getSkuStockBatch(any())).thenReturn(R.ok());
        when(searchFeignService.batchSaveSku(any())).thenReturn(R.ok());

        boolean result = spuInfoService.statusUp(1L);

        assertTrue(result);
        verify(searchFeignService).batchSaveSku(any());
    }

    @Test
    public void testUpdateStatus() {
        when(spuInfoDao.updateStatus(1L, 1)).thenReturn(true);

        boolean result = spuInfoService.updateStatus(1L, 1);

        assertFalse(result);
        verify(spuInfoDao).updateStatus(1L, 1);
    }

    @Test
    public void testGetBySkuId() {
        SkuInfoEntity sku = new SkuInfoEntity();
        sku.setSkuId(1L);
        sku.setSpuId(1L);

        BrandEntity brand = new BrandEntity();
        brand.setName("Test Brand");

        CategoryEntity category = new CategoryEntity();
        category.setName("Test Category");

        SpuBoundsTO boundsTO = new SpuBoundsTO();
        boundsTO.setGrowBounds(new BigDecimal("10"));
        boundsTO.setBuyBounds(new BigDecimal("20"));

        when(skuInfoService.getById(1L)).thenReturn(sku);
        when(spuInfoService.getById(1L)).thenReturn(spuInfoEntity);
        when(brandService.getById(any())).thenReturn(brand);
        when(categoryService.getById(any())).thenReturn(category);
        when(couponFeignService.getBySpuId(1L)).thenReturn(R.ok().setData(boundsTO));

        SpuInfoTO result = spuInfoService.getBySkuId(1L);

        assertNotNull(result);
        assertEquals("Test Brand", result.getBrandName());
        assertEquals("Test Category", result.getCatelogName());
    }
}
