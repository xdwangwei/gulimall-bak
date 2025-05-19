import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.SpuCommentDao;
import com.vivi.gulimall.product.entity.SpuCommentEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpuCommentServiceImplTest {

    @Mock
    private SpuCommentDao spuCommentDao;

    @InjectMocks
    private SpuCommentServiceImpl spuCommentService;

    @Mock
    private IPage<SpuCommentEntity> mockPage;

    private Map<String, Object> params;

    @Before
    public void setUp() {
        params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
    }

    @Test
    public void testQueryPage() {
        // Given
        when(spuCommentService.page(any(), any())).thenReturn(mockPage);
        when(mockPage.getTotal()).thenReturn(100L);
        when(mockPage.getPages()).thenReturn(10L);
        when(mockPage.getCurrent()).thenReturn(1L);
        when(mockPage.getSize()).thenReturn(10L);

        // When
        PageUtils result = spuCommentService.queryPage(params);

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getTotalCount());
        assertEquals(10L, result.getTotalPage());
        assertEquals(1L, result.getCurrPage());
        assertEquals(10L, result.getPageSize());
    }

    @Test
    public void testQueryPageWithEmptyParams() {
        // Given
        Map<String, Object> emptyParams = new HashMap<>();
        when(spuCommentService.page(any(), any())).thenReturn(mockPage);

        // When
        PageUtils result = spuCommentService.queryPage(emptyParams);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testQueryPageWithNullParams() {
        // Given
        when(spuCommentService.page(any(), any())).thenReturn(mockPage);

        // When
        PageUtils result = spuCommentService.queryPage(null);

        // Then
        assertNotNull(result);
    }
}
