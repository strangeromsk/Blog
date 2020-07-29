--INSERT users, all passwords are 'password'
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1593550800', 'Ivan', 'ivan@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '111111', 'dsfsdfsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (1, '1554066000', 'Semen', 'semen@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '11111', 'dsfsdfsdfe.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1593550800', 'Petr', 'petr@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '111331', 'dsfsd33fsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '1585688400', 'Egor', 'egor@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '1111311', 'ds3332sdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (1, '1554066000', 'Arkadiy', 'arkasha@example.com', '$2y$12$99Sc4oEyBLMXqdUxbExcnu3qdBdZRHh5bn.InU7xE5psl3JfH1qhe', '1111311', 'ds3332sdf.img');

--INSERT tags
INSERT INTO tags (name) VALUES ('Spring');
INSERT INTO tags (name) VALUES ('Hibernate');
INSERT INTO tags (name) VALUES ('StreamAPI');

--INSERT posts
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '1554066000', 'main page', 'Sed ut perspiciatis unde omnis', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1554066000', 'alalalala', 'iste natus error sit voluptatem accusantium doloresi architecto beata', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 1, 'NEW', 1, '1585688400', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1585688400', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'DECLINED', 1, '1585688400', 'main page', 'blabla', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'NEW', 1, '1593550800', 'alalalala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '1593550800', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1606770000', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '1607461200', 'main page', 'blabla', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1604869200', 'alalalala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '1604869200', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '1604869200', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1591650000', 'alalalala', 'blabla20000', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1528491600', 'alala121lala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '1496955600', 'alalala12la', 'blab222la2', 1500);

--INSERT tag2post
INSERT INTO tag2post (post_id, tag_id) VALUES (1,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (1,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (2,2);
INSERT INTO tag2post (post_id, tag_id) VALUES (2,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (3,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (4,1);

--INSERT post_comments
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (4, 1, 0, '1593550800', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (3, 1, 0, '1496955600', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (2, 3, 0, '1604869200', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (1, 1, 0, '1593550800', 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (1, 3, 0, '1593550800', 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');

--INSERT post_votes
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (4, 1, '1593550800', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (3, 1, '1593550800', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (2, 2, '1496955600', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (1, 4, '1593550800', -1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (4, 1, '1604869200', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (3, 1, '1585688400', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (2, 2, '1585688400', 1);

--INSERT global_settings
INSERT INTO global_settings (code, name, value) VALUES ('MULTIUSER_MODE', 'multiuser mode', true);
INSERT INTO global_settings (code, name, value) VALUES ('POST_PREMODERATION', 'post premoderation', true);
INSERT INTO global_settings (code, name, value) VALUES ('STATISTICS_IS_PUBLIC', 'show blog stat to everyone', true);

--INSERT captcha_codes
INSERT INTO captcha_codes (time, code, secret_code) VALUES ('1585688400', '1234', 1);