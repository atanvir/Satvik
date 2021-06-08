package com.satvick.model;

public class ShippingChargesModel {
    private String status;
    private ChargesModel getshippingcharges;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChargesModel getGetshippingcharges() {
        return getshippingcharges;
    }

    public void setGetshippingcharges(ChargesModel getshippingcharges) {
        this.getshippingcharges = getshippingcharges;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class ChargesModel{
        private PaymentModel payment;

        public PaymentModel getPayment() {
            return payment;
        }

        public void setPayment(PaymentModel payment) {
            this.payment = payment;
        }

        public class PaymentModel{
            private String amount;
            private String shipping;
            private String minus_shipping;
            private String payableWithShipAndGift;
            private String discount;
            private String giftwrap;
            private String payable;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getShipping() {
                return shipping;
            }

            public void setShipping(String shipping) {
                this.shipping = shipping;
            }

            public String getMinus_shipping() {
                return minus_shipping;
            }

            public void setMinus_shipping(String minus_shipping) {
                this.minus_shipping = minus_shipping;
            }

            public String getPayableWithShipAndGift() {
                return payableWithShipAndGift;
            }

            public void setPayableWithShipAndGift(String payableWithShipAndGift) {
                this.payableWithShipAndGift = payableWithShipAndGift;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getGiftwrap() {
                return giftwrap;
            }

            public void setGiftwrap(String giftwrap) {
                this.giftwrap = giftwrap;
            }

            public String getPayable() {
                return payable;
            }

            public void setPayable(String payable) {
                this.payable = payable;
            }
        }
    }
}

