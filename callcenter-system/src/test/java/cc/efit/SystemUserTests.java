package cc.efit;

import cc.efit.modules.system.domain.Dept;
import cc.efit.modules.system.domain.User;
import cc.efit.modules.system.repository.UserRepository;
import cc.efit.modules.system.service.JobService;
import cc.efit.modules.system.service.UserService;
import cc.efit.modules.system.service.dto.JobDto;
import cc.efit.modules.system.service.dto.UserQueryCriteria;
import cc.efit.db.utils.QueryHelp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AppRun.class)
public class SystemUserTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobService jobService;
    @Test
    public void testFindUser() {
        Integer id = 2 ;
        User user = userRepository.findByUsername("user");
        Dept dept = new Dept();
        dept.setId(28);
        user.setDept(dept);
        userRepository.save(user);
        System.out.println(user);
    }
    @Test
    public void testFindJob() {
        Integer id = 8 ;
        JobDto jbo = jobService.findById(id);
        System.out.println(jbo);
    }
    @Test
    public void testDelete() {
        Integer id =1 ;
        jobService.delete(Set.of(id));
    }
    @Test
    public void testFindByRoleId() {
        Integer id = 1 ;
        Pageable pageable = Pageable.ofSize(1);
        UserQueryCriteria criteria  = new UserQueryCriteria();
        criteria.setRoleId(id);
        Page<User> page = userRepository.findAll( (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent());
    }

}

