package com.satvick.model;

import java.util.List;

public class CategoryModel {
    private String status;
    private List<Categorylist> catandsubcat;


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public List<Categorylist> getCategory_name() {
        return catandsubcat;
    }

    public void setCategory_name(List<Categorylist> catandsubcat) {
        this.catandsubcat = catandsubcat;
    }

    public class Categorylist{
        private String cat_id;
        private String category_name;
        private String cat_image;
        private List<SubCategoryModel> subcategory;

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getCat_image() {
            return cat_image;
        }

        public void setCat_image(String cat_image) {
            this.cat_image = cat_image;
        }

        public List<SubCategoryModel> getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(List<SubCategoryModel> subcategory) {
            this.subcategory = subcategory;
        }


        public class SubCategoryModel{
            private String id;
            private String category_id;
            private String subcategory_name;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getSubcategory_name() {
                return subcategory_name;
            }

            public void setSubcategory_name(String subcategory_name) {
                this.subcategory_name = subcategory_name;
            }
        }
    }
}


