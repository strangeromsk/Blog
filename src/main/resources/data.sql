--INSERT users
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '2020-01-01 00:00:10', 'Ivan', 'ivan@example.com', '123123dfsfdsf', '111111', 'dsfsdfsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '2020-04-04 00:00:10', 'Semen', 'semen@example.com', '123123dfeesfdsf', '11111', 'dsfsdfsdfe.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '2019-01-01 00:00:10', 'Petr', 'petr@example.com', '123fsfdsf', '111331', 'dsfsd33fsdf.img');
INSERT INTO users (is_moderator, reg_time, name, email, password, code, photo)
VALUES (0, '2020-01-01 00:00:10', 'Egor', 'egor@example.com', '123232323dfsfdsf', '1111311', 'ds3332sdf.img');

--INSERT tags
INSERT INTO tags (name) VALUES ('bla');
INSERT INTO tags (name) VALUES ('blabla');

--INSERT posts
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '2020-01-01 00:00:10', 'main page', 'blabla', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '2020-04-05 00:00:10', 'alalalala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 1, 'NEW', 1, '2020-10-01 00:00:10', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '2020-01-01 00:00:10', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'DECLINED', 1, '2020-01-02 00:00:10', 'main page', 'blabla', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'NEW', 1, '2020-03-05 00:00:10', 'alalalala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '2020-09-01 00:00:10', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'ACCEPTED', 1, '2020-07-01 00:00:10', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(1, 1, 'ACCEPTED', 1, '2020-01-01 00:00:10', 'main page', 'blabla', 20);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '2020-05-05 00:00:10', 'alalalala', 'blabla2', 1500);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(3, 0, 'NEW', 1, '2020-12-01 00:00:10', 'tralalala', '11111blabla', 9000);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(4, 1, 'DECLINED', 1, '2020-11-21 00:00:10', 'main234324 page', 'bla234324bla', 2005);
INSERT INTO posts (user_id, is_active, moderation_status, moderator_id, time, title, text, view_count) VALUES
(2, 1, 'ACCEPTED', 1, '2020-05-05 00:00:10', 'alalalala', 'blabla20000', 1500);

--INSERT tag2post
INSERT INTO tag2post (post_id, tag_id) VALUES (1,1);
INSERT INTO tag2post (post_id, tag_id) VALUES (2,2);

--INSERT post_comments
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (4, 1, 0, '2020-02-01 00:00:10', 'go f*ck urself');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (3, 1, 0, '2020-10-03 00:00:10', 'dog gammit');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (2, 2, 0, '2020-11-01 00:00:10', 'you damn fool');
INSERT INTO post_comments (user_id, post_id, parent_id, time, text) VALUES (1, 1, 0, '2020-10-01 00:00:10', 'dont shit yourself');

--INSERT post_votes
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (4, 1, '2020-10-01 00:00:10', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (3, 1, '2020-10-01 00:11:10', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (2, 2, '2020-10-01 00:00:10', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (1, 3, '2020-10-01 00:00:10', -1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (4, 1, '2020-10-01 00:00:10', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (3, 1, '2020-10-01 00:11:10', 1);
INSERT INTO post_votes (user_id, post_id, time, value) VALUES (2, 2, '2020-10-01 00:00:10', 1);

--INSERT global_settings
INSERT INTO global_settings (code, name, value) VALUES ('12', 'change identity', 1);

--INSERT captcha_codes
INSERT INTO captcha_codes (time, code, secret_code) VALUES ('2020-10-01 00:00:10', '1234', 1);