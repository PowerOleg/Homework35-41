package ru.netology.Homework40.springMVC.repositories;

import org.springframework.stereotype.Repository;
import ru.netology.Homework40.springMVC.exception.NotFoundException;
import ru.netology.Homework40.springMVC.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final ConcurrentHashMap<Long, Post> posts;
    private final AtomicLong idCounter = new AtomicLong(0L);

    public PostRepositoryImpl() {
        posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values()).stream().filter(n -> !n.isRemoved()).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (posts.get(id).isRemoved()) {
            return Optional.empty();
        }
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() != 0) {
            posts.put(post.getId(), post);
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
            final Post post = posts.get(id);
            post.setRemoved(true);
            posts.put(id, post);
        } else {
            throw new NotFoundException("Wrong id");
        }
    }
}