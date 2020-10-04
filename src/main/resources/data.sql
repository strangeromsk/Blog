--INSERT users, all passwords are 'password'
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1593550800', 'Ivan', 'ivan@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '111111', 'dsfsdfsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (1, '1554066000', 'Semen', 'semen@example.com', '$2y$12$x2LTE0vcL1FvQFR2aXHN7erDNwiO/yqKnyQUjiGKkohMEG/yxg.Wm', '11111', 'dsfsdfsdfe.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1593550800', 'Petr', 'petr@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '111331', 'dsfsd33fsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1585688400', 'Egor', 'egor@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '1111311', 'ds3332sdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (1, '1554066000', 'Arkadiy', 'arkasha@example.com', '$2y$12$x2LTE0vcL1FvQFR2aXHN7erDNwiO/yqKnyQUjiGKkohMEG/yxg.Wm', '1111311', 'ds3332sdf.img');

--INSERT tags
INSERT INTO tags (name) VALUES ('Spring');
INSERT INTO tags (name) VALUES ('Hibernate');
INSERT INTO tags (name) VALUES ('StreamAPI');

--INSERT posts
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '1491771600', 'Lorem ipsum', 'Sed ut perspiciatis unde omnis', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1491771600', 'Ipsum', 'iste natus error sit voluptatem accusantium doloresi architecto beata', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(3, 1, 'NEW', 1, '1491771600', 'lorem', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, ', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1523394000', 'voluptate velit esse', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, ', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(1, 1, 'DECLINED', 1, '1589144400', 'Sed ut perspiciatis', 'Excepteur sint occaecat cupidatat non', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'NEW', 1, '1589144400', 'Sed ut perspiciatis', 'exercitationem ullam corporis suscipit laboriosam', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '1589144400', 'voluptatibus maiores', 'Excepteur sint occaecat cupidatat non', 9900);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1557522000', 'asperiores repellat.', 'voluptatem, quia voluptas sit, aspernat', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '1557522000', 'non recusandae', 'voluptatem, quia voluptas sit, aspernat', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1560546000', 'molestiae consequatu', 'exercitationem ullam corporis', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '1571086800', 'tralalala', 'exercitationem ullam corporis', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1565816400', 'Nam libero tempore', 'similique sunt in culpa, qui off', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1591650000', 'non recusandae', 'similique sunt in culpa, qui off', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1528491600', 'non recusandae', 'similique sunt in culpa, qui off', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1496955600', 'oluptatibus maiores alias consequatur aut perfer', 'similique sunt in culpa, qui off', 1500);

--16thpost below //15 are above
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'StreamAPI is the best way to change your collections!', 7576);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955700', 'Always use hibernate', 'Best possible way to map your entities is hibernate ORM', 15051);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'error sit voluptatem accusantium', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'error sit voluptatem accusantium', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'error sit voluptatem accusantium', 7576);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 7576);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 15051);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 7576);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 5, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 15051);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, timestamp, title, text, view_count) VALUES
(5, 1, 'ACCEPTED', 1, '1496955600', 'StreamAPI is the best', 'Sed ut perspiciatis unde omnis', 1500);

--INSERT tag2post
INSERT INTO tag2post (post_id, tag_id) VALUES (1,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (1,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (2,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (2,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (3,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (4,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (16,3);
INSERT INTO tag2post (post_id, tag_id) VALUES (17,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (16,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (20,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (20,3);

--INSERT post_comments
INSERT INTO post_comments (user_id, post_id, parent_id, timestamp, text) VALUES (4, 1, 0, '1593550800', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO post_comments (user_id, post_id, parent_id, timestamp, text) VALUES (3, 1, 0, '1496955600', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat');
INSERT INTO post_comments (user_id, post_id, parent_id, timestamp, text) VALUES (2, 3, 0, '1604869200', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur');
INSERT INTO post_comments (user_id, post_id, parent_id, timestamp, text) VALUES (1, 1, 0, '1593550800', 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');
INSERT INTO post_comments (user_id, post_id, parent_id, timestamp, text) VALUES (1, 3, 0, '1593550800', 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');

--INSERT post_votes
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (4, 1, '1593550800', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (3, 1, '1593550800', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (2, 2, '1496955600', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (1, 4, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (4, 1, '1604869200', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (3, 1, '1585688400', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (2, 4, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (3, 16, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (4, 16, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (5, 16, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (1, 16, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (3, 2, '1585688400', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (4, 2, '1585688400', 1);
INSERT INTO post_votes (user_id, post_id, timestamp, value) VALUES (5, 2, '1585688400', 1);

--INSERT global_settings
INSERT INTO global_settings (code, name, value) VALUES ('MULTIUSER_MODE', 'multiuser mode', true);
INSERT INTO global_settings (code, name, value) VALUES ('POST_PREMODERATION', 'post premoderation', true);
INSERT INTO global_settings (code, name, value) VALUES ('STATISTICS_IS_PUBLIC', 'show blog stat to everyone', true);

--INSERT captcha_codes
INSERT INTO captcha_codes (timestamp, code, secret_code) VALUES ('1585688400', '1234', 1);