import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vivi.common.utils.PageUtils;
import com.vivi.gulimall.product.dao.CategoryBrandRelationDao;
import com.vivi.gulimall.product.dao.CategoryDao;
import com.vivi.gulimall.product.entity.CategoryEntity;
import com.vivi.gulimall.product.vo.Catelog2VO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @Before
    public void setup() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testListWithTree() {
        // Setup
        List<CategoryEntity> allCategories = new ArrayList<>();

        CategoryEntity cat1 = new CategoryEntity();
        cat1.setCatId(1L);
        cat1.setParentCid(0L);
        cat1.setSort(1);

        CategoryEntity cat2 = new CategoryEntity();
        cat2.setCatId(2L);
        cat2.setParentCid(1L);
        cat2.setSort(2);

        allCategories.add(cat1);
        allCategories.add(cat2);

        when(categoryDao.selectList(null)).thenReturn(allCategories);

        // Execute
        List<CategoryEntity> result = categoryService.listWithTree();

        // Verify
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1), result.get(0).getCatId());
        assertEquals(1, result.get(0).getChildren().size());
    }

    @Test
    public void testRemoveBatchByIds() {
        // Setup
        List<Long> ids = Arrays.asList(1L, 2L);

        // Execute
        boolean result = categoryService.removeBatchByIds(ids);

        // Verify
        verify(categoryDao).deleteBatchIds(ids);
        assertTrue(result);
    }

    @Test
    public void testFindCategoryPath() {
        // Setup
        CategoryEntity cat1 = new CategoryEntity();
        cat1.setCatId(1L);
        cat1.setParentCid(0L);

        CategoryEntity cat2 = new CategoryEntity();
        cat2.setCatId(2L);
        cat2.setParentCid(1L);

        when(categoryDao.selectById(2L)).thenReturn(cat2);
        when(categoryDao.selectById(1L)).thenReturn(cat1);

        // Execute
        List<Long> path = categoryService.findCategoryPath(2L);

        // Verify
        assertEquals(Arrays.asList(1L, 2L), path);
    }

    @Test
    public void testUpdateCascadeById() {
        // Setup
        CategoryEntity category = new CategoryEntity();
        category.setCatId(1L);
        category.setName("Updated Name");

        // Execute
        boolean result = categoryService.updateCascadeById(category);

        // Verify
        verify(categoryDao).updateById(category);
        verify(categoryBrandRelationDao).updateCategoryName(1L, "Updated Name");
        assertTrue(result);
    }

    @Test
    public void testGetLevel1Categories() {
        // Setup
        List<CategoryEntity> level1Categories = new ArrayList<>();
        CategoryEntity cat = new CategoryEntity();
        cat.setCatId(1L);
        cat.setParentCid(0L);
        level1Categories.add(cat);

        when(categoryDao.selectList(any(QueryWrapper.class))).thenReturn(level1Categories);

        // Execute
        List<CategoryEntity> result = categoryService.getLevel1Categories();

        // Verify
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1), result.get(0).getCatId());
    }

    @Test
    public void testGetCatelogJson() {
        // Setup
        List<CategoryEntity> allCategories = new ArrayList<>();
        CategoryEntity cat1 = new CategoryEntity();
        cat1.setCatId(1L);
        cat1.setParentCid(0L);
        cat1.setName("Category 1");

        CategoryEntity cat2 = new CategoryEntity();
        cat2.setCatId(2L);
        cat2.setParentCid(1L);
        cat2.setName("Category 2");

        allCategories.add(cat1);
        allCategories.add(cat2);

        when(categoryDao.selectList(null)).thenReturn(allCategories);

        // Execute
        Map<String, List<Catelog2VO>> result = categoryService.getCatelogJson();

        // Verify
        assertNotNull(result);
    }
}
