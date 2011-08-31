/**
 * Contains classes of a packet-based, polled, high performance and highly scalable networking layer.
 * Additional feature-enabling classes, such as a flood manager, an idle connection dropper,
 * a lazy, concurrent, entry compacting logger are also contained in this package.
 * <BR><BR>
 * In order to make use of this networking layer when creating a server/client application that uses
 * a packet-based protocol, you will need to extend/implement these classes:
 * <UL>
 * <LI>{@link com.l2jfree.network.mmocore.MMOConnection}</LI>
 * <LI>{@link com.l2jfree.network.mmocore.MMOController}</LI>
 * <LI>{@link com.l2jfree.network.mmocore.PacketHandler}</LI>
 * <LI>{@link com.l2jfree.network.mmocore.ReceivablePacket}</LI>
 * <LI>{@link com.l2jfree.network.mmocore.SendablePacket}</LI>
 * </UL>
 * <BR><BR>
 * This package is based on a reference MMOCore by KenM
 * (currently managed by <A href="http://www.l2jserver.com/">L2JServer</A>).
 */
package com.l2jfree.network.mmocore;
