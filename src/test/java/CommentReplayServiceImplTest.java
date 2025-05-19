import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vivi.common.utils.PageUtils;
import com.vivi.common.utils.Query;
import com.vivi.gulimall.product.dao.CommentReplayDao;
import com.vivi.gulimall.product.entity.CommentReplayEntity;
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
public class CommentReplayServiceImplTest {

    @Mock
    private CommentReplayDao commentReplayDao;

    @InjectMocks
    private CommentReplayServiceImpl commentReplayService;

    @Before
    public void setUp() {
        // Common setup if needed
    }

    @Test
    public void testQueryPage() {
        // Prepare test data
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");

        List<CommentReplayEntity> records = new ArrayList<>();
        CommentReplayEntity entity = new CommentReplayEntity();
        entity.setId(1L);
        entity.setCommentId(100L);
        entity.setReplyId(200L);
        records.add(entity);

        IPage<CommentReplayEntity> page = new Page<>();
        page.setRecords(records);
        page.setTotal(1);
        page.setCurrent(1);
        page.setSize(10);

        // Mock behavior
        when(commentReplayDao.selectPage(any(), any(QueryWrapper.class))).thenReturn(page);

        // Execute method
        PageUtils result = commentReplayService.queryPage(params);

        // Verify results
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getCurrPage());
        assertEquals(10, result.getPageSize());
    }

    @Test
    public void testQueryPageWithEmptyParams() {
        // Prepare test data
        Map<String, Object> params = new HashMap<>();

        IPage<CommentReplayEntity> page = new Page<>();
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        // Mock behavior
        when(commentReplayDao.selectPage(any(), any(QueryWrapper.class))).thenReturn(page);

        // Execute method
        PageUtils result = commentReplayService.queryPage(params);

        // Verify results
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        assertTrue(result.getList().isEmpty());
    }
}
