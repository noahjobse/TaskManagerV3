-- Schema definition
CREATE SCHEMA IF NOT EXISTS public;

-- Drop the tables if they exist (to avoid errors during recreation)
DROP TABLE IF EXISTS public.Task_Categories;
DROP TABLE IF EXISTS public.Reminders;
DROP TABLE IF EXISTS public.Tasks;
DROP TABLE IF EXISTS public.Categories;
DROP TABLE IF EXISTS public.Users;

-- Table: Users
CREATE TABLE public.Users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL
);

-- Table: Categories
CREATE TABLE public.Categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);

-- Table: Tasks
CREATE TABLE public.Tasks (
    task_id SERIAL PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,
    status VARCHAR(50),
    user_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.Users(user_id)
);

-- Table: Reminders
CREATE TABLE public.Reminders (
    reminder_id SERIAL PRIMARY KEY,
    reminder_date_time TIMESTAMP NOT NULL,
    is_complete BOOLEAN DEFAULT FALSE,
    recurring BOOLEAN DEFAULT FALSE,
    repeat_frequency VARCHAR(50),
    task_id INT NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES public.Tasks(task_id)
);

-- Table: Task_Categories (Junction Table)
CREATE TABLE public.Task_Categories (
    task_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (task_id, category_id),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES public.Tasks(task_id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES public.Categories(category_id)
);
