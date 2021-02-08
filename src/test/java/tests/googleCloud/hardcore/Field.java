package tests.googleCloud.hardcore;

public enum Field {
   QUANTITY("quantity", "4"),
   LABEL("label", ""),
   OS("os", "free"),
   CLASS("class", "regular", "VM class: regular"),
   SERIES("series", "n1"),
   INSTANCE("instance", "CP-COMPUTEENGINE-VMIMAGE-N1-STANDARD-8", "Instance type: n1-standard-8"),
   ADD_GPU("addGPUs"),
   GPU_COUNT("gpuCount", "1"),
   GPU_TYPE("gpuType", "NVIDIA_TESLA_V100"),
   SSD("ssd", "2", "Total available local SSD space 2x375 GiB"),
   LOCATION("location", "europe-west3", "Region: Frankfurt"),
   SERVER_CUD("cud", "1", "Commitment term: 1 Year");

   private String name;
   private String value;
   private String nameInFinalList;

   public static Field[] getAllEnumFieldsAsArray() {
      return Field.class.getEnumConstants();
   }

   public String getName() {
      return name;
   }

   public String getValue() {
      return value;
   }

   public String getNameInFinalList() {
      return nameInFinalList;
   }

   Field(String name, String value, String nameInFinalList) {
      this.name = name;
      this.value = value;
      this.nameInFinalList = nameInFinalList;
   }

   Field(String name, String value) {
      this.name = name;
      this.value = value;
   }

   Field(String name) {
      this.name = name;
   }
}
