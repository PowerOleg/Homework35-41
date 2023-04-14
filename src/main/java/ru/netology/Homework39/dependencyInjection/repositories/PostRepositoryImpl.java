package ru.netology.Homework39.dependencyInjection.repositories;



import org.springframework.stereotype.Repository;
import ru.netology.Homework39.dependencyInjection.exception.NotFoundException;
import ru.netology.Homework39.dependencyInjection.models.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final ConcurrentHashMap<Long, Post> posts;
    private final AtomicLong idCounter = new AtomicLong(0L);

    public PostRepositoryImpl()  {
        posts  = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if(post.getId() != 0) {
            if (!posts.containsKey(post.getId())) {
                throw new NotFoundException();
            } else {
                posts.put(post.getId(), post);
            }
        }

        if (post.getId() == 0) {
            var newId = idCounter.incrementAndGet();
            post.setId(newId);
            posts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException("Wrong id");
        }
    }
}