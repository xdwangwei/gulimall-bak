import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.SpuImagesDao;
import com.vivi.gulimall.product.entity.SpuImagesEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SpuImagesServiceImplTest {

    @Mock
    private SpuImagesDao spuImagesDao;

    @InjectMocks
    private SpuImagesServiceImpl spuImagesService;

    private SpuImagesEntity spuImage;
    private List<SpuImagesEntity> spuImageList;
    private Map<String, Object> params;

    @Before
    public void setUp() {
        spuImage = new SpuImagesEntity();
        spuImage.setId(1L);
        spuImage.setSpuId(100L);
        spuImage.setImgName("test.jpg");
        spuImage.setImgUrl("http://test.com/test.jpg");
        spuImage.setImgSort(1);
        spuImage.setDefaultImg(1);

        spuImageList = new ArrayList<>();
        spuImageList.add(spuImage);

        params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
    }

    @Test
    public void testQueryPage() {
        IPage<SpuImagesEntity> page = new Page<>();
        page.setRecords(spuImageList);
        page.setTotal(1);

        when(spuImagesDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = spuImagesService.queryPage(params);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getList().size());

        SpuImagesEntity resultImage = (SpuImagesEntity) result.getList().get(0);
        assertEquals(spuImage.getId(), resultImage.getId());
        assertEquals(spuImage.getSpuId(), resultImage.getSpuId());
        assertEquals(spuImage.getImgName(), resultImage.getImgName());
        assertEquals(spuImage.getImgUrl(), resultImage.getImgUrl());
        assertEquals(spuImage.getImgSort(), resultImage.getImgSort());
        assertEquals(spuImage.getDefaultImg(), resultImage.getDefaultImg());
    }

    @Test
    public void testQueryPageWithEmptyResult() {
        IPage<SpuImagesEntity> page = new Page<>();
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        when(spuImagesDao.selectPage(any(), any())).thenReturn(page);

        PageUtils result = spuImagesService.queryPage(params);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        assertTrue(result.getList().isEmpty());
    }
}
