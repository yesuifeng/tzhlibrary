package banner;

import base.BaseEntity;

/**
 * Created by GaoJian on 2018/4/24.
 */
public class BannerEntity extends BaseEntity {
    public String pic;
    public String title;

    public BannerEntity() {
    }
    public BannerEntity(String pic) {
        this.pic = pic;
    }
}
