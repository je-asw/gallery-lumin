-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, pass, first_name, last_name, email)
VALUES (:id, :pass, :first_name, :last_name, :email);

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id;

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id;

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id;

-- :name create-auth!
-- :doc creates a new user record
INSERT INTO users (id, pass)
VALUES (:id, :pass);

-- :name get-users :? :*
-- :doc retrieves a users record
SELECT * FROM users
order by id
