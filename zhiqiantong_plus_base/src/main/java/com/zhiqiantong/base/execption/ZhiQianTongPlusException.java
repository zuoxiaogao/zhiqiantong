package com.zhiqiantong.base.execption;


/**
 * @description 职前通项目异常类
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
public class ZhiQianTongPlusException extends RuntimeException {

   private String errMessage;

   public ZhiQianTongPlusException() {
      super();
   }

   public ZhiQianTongPlusException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public static void cast(CommonError commonError){
       throw new ZhiQianTongPlusException(commonError.getErrMessage());
   }
   public static void cast(String errMessage){
       throw new ZhiQianTongPlusException(errMessage);
   }

}
