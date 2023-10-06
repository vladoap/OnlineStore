-- addresses
INSERT INTO addresses (city, street_name, street_number, country)
VALUES ('Plovdiv', 'Vasil Aprilov', '150', 'Bulgaria');
INSERT INTO addresses (city, street_name, street_number, country)
VALUES ('Plovdiv', 'Macedoina', '32', 'Bulgaria');
INSERT INTO addresses (city, street_name, street_number, country)
VALUES ('Sofia', 'Bulgaria', '100', 'Bulgaria');
INSERT INTO addresses (city, street_name, street_number, country)
VALUES ('Sofia', 'Vitosha', '5', 'Bulgaria');
INSERT INTO addresses (city, street_name, street_number, country)
VALUES ('Sofia', 'Vitosha', '10', 'Bulgaria');

-- pictures
INSERT INTO pictures(created, public_id, title, url)
VALUES (now(), 'memqqc2skf4t2emkppxk', 'profile3', 'https://res.cloudinary.com/dkhgpyqjy/image/upload/v1696598635/memqqc2skf4t2emkppxk.png');
INSERT INTO pictures(created, public_id, title, url)
VALUES (now(), 'iebquo9jwxnswqzdde69', 'profile1', 'https://res.cloudinary.com/dkhgpyqjy/image/upload/v1696598635/iebquo9jwxnswqzdde69.png');
INSERT INTO pictures(created, public_id, title, url)
VALUES (now(), 'zvq109dgmckxxiw0kmfw', 'profile2', 'https://res.cloudinary.com/dkhgpyqjy/image/upload/v1696598635/zvq109dgmckxxiw0kmfw.png');

-- user roles
INSERT INTO roles (id, name)
VALUES (1, 'ADMIN');
INSERT INTO roles (id, name)
VALUES (2, 'USER');


-- product categories
INSERT INTO categories (created, description, name)
VALUES (now(), 'Explore cutting-edge technology, from sleek smartphones to powerful laptops and everything in between.
Whether you are a tech enthusiast or seeking essential gadgets, our electronics category offers a world of possibilities to
    enhance your digital lifestyle. Stay connected, entertained, and productive with our top-notch electronic devices and accessories.',
        'Electronics');

INSERT INTO categories (created, description, name)
VALUES (now(), 'Discover the latest trends and timeless classics in clothing, footwear, and accessories. From everyday essentials
to statement pieces, our fashion collection allows you to curate a wardrobe that reflects your personal style.
Elevate your fashion game and make a statement wherever you go.',
        'Fashion');

INSERT INTO categories (created, description, name)
VALUES (now(), 'Explore a wide range of home essentials, from furniture that complements your lifestyle to exquisite decor
that reflects your unique taste. Elevate your living experience, one room at a time, with our curated selection of quality products designed to make your house a true home.',
        'Home');

INSERT INTO categories (created, description, name)
VALUES (now(), 'Welcome to our literary haven, where you can embark on journeys of imagination, knowledge, and inspiration.
Dive into a diverse selection of reading materials, including fiction, non-fiction, educational resources, and more.
Discover the magic of storytelling and the joy of learning within the pages of our carefully curated books.',
        'Books');


-- users
INSERT INTO users (created, email, first_name, last_name, password, username, address_city, address_street_name, address_street_number, profile_picture_id)
VALUES (now(), 'gosho@gosho.bg', 'Georgi', 'Ivanov', '111', 'gosho', 'Plovdiv', 'Macedonia', 32, 2);
INSERT INTO users (created, email, first_name, last_name, password, username, address_city, address_street_name, address_street_number, profile_picture_id)
VALUES (now(), 'peter@todorv.bg', 'Peter', 'Petrov', '111', 'pesho', 'Sofia', 'Vitosha', 5, 3);

-- users_roles
INSERT INTO users_roles VALUES (1, 1);
INSERT INTO users_roles VALUES (1, 2);
INSERT INTO users_roles VALUES (2, 2);











