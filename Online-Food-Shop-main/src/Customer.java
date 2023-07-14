public class Customer {
    // Variabel username untuk customer
    private static String username = "customer";
    // Variabel password untuk customer
    private static String password = "customer";

    // Getter username untuk customer
    public static String getUsername() {
        return username;
    }

    // Getter password untuk customer
    public static String getPassword() {
        return password;
    }

    // Method menu untuk customer
    public static void menu() {
        App.clearScreen();
        // Tampilkan menu
        System.out.println(App.BOLD + "\nSelamat datang, Customer!" + App.NORMAL);
        System.out.println("---------------");
        System.out.println("1. Lihat restoran");
        System.out.println("2. Tambah pesanan");
        System.out.println("3. Lihat pesanan");
        System.out.println("4. Hapus pesanan");
        System.out.println("5. Bayar");
        System.out.println("0. Kembali ke menu Login");

        // Input pilihan menu
        System.out.print("(Mohon masukkan kode angka untuk melanjutkan)\n> ");
        int select = Input.getInteger(0, 5);

        // Jalankan menu yang dipilih
        switch (select) {
            case 1:
                Data.showRestaurant();
                break;
            case 2:
                addOrder();
                break;
            case 3:
                App.clearScreen();
                Data.showOrder();
                break;
            case 4:
                removeOrder();
                break;
            case 5:
                payOrder();
                break;
            case 0:
                App.login();
                return;
        }

        // Kembali ke menu
        menu();
        return;
    }

    public static void addOrder() {
        App.clearScreen();
        // Tampilkan restoran yang teregistrasi
        int idRestaurant = Data.showRestaurant();
        if (idRestaurant == 0) {
            return;
        }

        while (true) {
            // Input ID makanan atau minuman
            System.out.println(App.BOLD + "\nTambahkan Pesanan" + App.NORMAL);
            System.out.println("---------------");
            System.out.println("Mohon masukkan ID menu (ex: 1-F)");
            System.out.print("*(Masukkan 0 untuk kembali)\n> ");

            // Deklarasi variabel untuk kebutuhan instansiasi objek Order
            String idMenu;
            int numberMenu;
            String categoryMenu;
            boolean isFood;

            // Input ID menu
            while (true) {
                idMenu = Input.getString();
                if (idMenu.equals("0")) {
                    menu();
                    return;
                } else {
                    // Validasi ID menu
                    if (idMenuValidation(idRestaurant, idMenu)) {
                        idMenu = idMenu.replace("\\s", "");
                        numberMenu = Integer.parseInt(idMenu.split("-")[0]);
                        categoryMenu = idMenu.split("-")[1].toUpperCase();
                        if (categoryMenu.equals("F")) {
                            isFood = true;
                        } else {
                            isFood = false;
                        }
                        break;
                    } else {
                        System.out.print("Input invalid. Mohon ulangi.\n> ");
                    }
                }
            }

            // Input jumlah pesanan
            System.out.println("Mohon masukkan jumlah pesanan.");
            System.out.print("*(Masukkan 0 untuk kembali)\n> ");
            int quantity = Input.getInteger(0, Integer.MAX_VALUE);
            if (quantity == 0) {
                menu();
                return;
            }

            // Input jarak antar
            System.out.println("Mohon masukkan jarak restoran ke lokasi antar (dalam KM).");
            System.out.print("*(Masukkan 0 untuk kembali)\n> ");
            Double distance = Input.getDouble(0, Double.MAX_VALUE);
            if (distance == 0.0) {
                menu();
                return;
            }

            // Instansiasi objek order
            if (isFood) {
                Data.addOrder(new Order(
                        Data.getRestaurants().get(idRestaurant - 1).getName(),
                        Data.getRestaurants().get(idRestaurant - 1).getAddress(),
                        Data.getRestaurants().get(idRestaurant - 1).getFoods().get(numberMenu - 1).get(0),
                        Double.parseDouble(
                                Data.getRestaurants().get(idRestaurant - 1).getFoods().get(numberMenu - 1).get(1)),
                        isFood,
                        quantity,
                        distance));
            } else {
                Data.addOrder(new Order(
                        Data.getRestaurants().get(idRestaurant - 1).getName(),
                        Data.getRestaurants().get(idRestaurant - 1).getAddress(),
                        Data.getRestaurants().get(idRestaurant - 1).getDrinks().get(numberMenu - 1).get(0),
                        Double.parseDouble(
                                Data.getRestaurants().get(idRestaurant - 1).getDrinks().get(numberMenu - 1).get(1)),
                        isFood,
                        quantity,
                        distance));
            }

            // Tampilkan data pesanan
            Data.showOrder();

            System.out.println("\n1. Tambah pesanan");
            System.out.println("2. Lihat restoran lainnya");
            System.out.println("0. Kembali ke menu utama");
            System.out.print("*(Mohon masukkan kode angka untuk melanjutkan)\n> ");

            // Input pilihan lanjutan
            int again = Input.getInteger(0, 2);
            switch (again) {
                case 1:
                    break;
                case 2:
                    addOrder();
                    return;
                case 0:
                    menu();
                    return;
            }
        }

    }

    // Method validasi ID menu
    public static boolean idMenuValidation(int idRestaurant, String idMenu) {
        idMenu = idMenu.replaceAll("\\s", "");
        String[] parseIdMenu = idMenu.split("-");
        if (parseIdMenu.length == 2) {
            try {
                int numberMenu = Integer.parseInt(parseIdMenu[0]);
                parseIdMenu[1] = parseIdMenu[1].toUpperCase();
                if (parseIdMenu[1].equals("F")) {
                    if (numberMenu > Data.getRestaurants().get(idRestaurant - 1).getFoods().size()) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (parseIdMenu[1].equals("D")) {
                    if (numberMenu > Data.getRestaurants().get(idRestaurant - 1).getDrinks().size()) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    // Method hapus pesanan
    public static void removeOrder() {
        App.clearScreen();
        // Tampilkan data pesanan
        Data.showOrder();

        System.out.println(App.BOLD + "\nHapus Pesanan" + App.NORMAL);
        System.out.println("---------------");

        // Input ID pesanan
        System.out.println("Mohon masukkan ID pesanan.");
        System.out.print("*(Masukkan 0 untuk kembali)\n> ");

        // Tampilkan data
        int idOrder = Input.getInteger(0, Data.getOrders().size());
        if (idOrder == 0) {
            menu();
            return;
        } else {
            System.out.printf(App.BOLD + "%d. %s-%s\t\t : %s\t\tRp. %.2f %dx \t\t : Rp. %.2f\n" + App.NORMAL,
                    idOrder,
                    Data.getOrders().get(idOrder - 1).getResaturantName(),
                    Data.getOrders().get(idOrder - 1).getResaturantAddress(),
                    Data.getOrders().get(idOrder - 1).getMenuName(),
                    Data.getOrders().get(idOrder - 1).getMenuPrice(),
                    Data.getOrders().get(idOrder - 1).getQuantity(),
                    Data.getOrders().get(idOrder - 1).getTotalPrice());
            System.out.print("*(Masukkan 1 untuk hapus dan 0 untuk kembali ke menu)\n> ");
            int confirm = Input.getInteger(0, 1);
            if (confirm == 0) {
                menu();
                return;
            } else {
                Data.removeOrder(idOrder - 1);
                System.out.println("Data berhasil dihapus.");
                System.out.print("*(Masukkan apapun untuk kembali)");
                Input.getAnyKey();
                menu();
                return;
            }
        }
    }

    // Method pembayaran pesanan
    public static void payOrder() {
        App.clearScreen();
        // Tampilkan nota pesanan
        System.out.println(App.BOLD + "\nNota Pesanan" + App.NORMAL);
        System.out.println("---------------");
        System.out.printf(App.BOLD + "\nUsername : %s\n" + App.NORMAL, Customer.getUsername());
        Double totalPayment = Data.showOrder();
        if (totalPayment == 0.0) {
            return;
        }
        System.out.printf(App.BOLD + "Kode Pembayaran : %s\n" + App.NORMAL,
                App.generateRandomString(20));

        System.out.print("*(Masukkan 1 untuk bayar dan 0 untuk kembali ke menu)\n> ");
        int pay = Input.getInteger(0, 1);
        if (pay == 0) {
            menu();
            return;
        }

        // Hapus semua data pesanan
        Data.removeAllOrders();
        System.out.println(
                App.BOLD + "Terima kasih telah memesan.\nMohon tunggu sampai pesanan Anda sampai." + App.NORMAL);
        menu();
        return;
    }
}
